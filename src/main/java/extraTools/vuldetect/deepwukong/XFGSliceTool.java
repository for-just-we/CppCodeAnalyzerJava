package extraTools.vuldetect.deepwukong;

import extraTools.vuldetect.utils.CallExprTool;
import extraTools.vuldetect.utils.SymbolizingTool;
import extraTools.vuldetect.utils.sinkPoints.DWKPoint;
import mainTool.ast.ASTNode;
import mainTool.ast.statements.jump.ReturnStatement;
import mainTool.cpg.CPG;
import mainTool.cpg.CodeEdge;

import com.ibm.icu.impl.Pair;
import mainTool.graphutils.Edge;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class XFGSliceTool {
    private List<CPG> cpgs;
    private Map<String, CPG> funcName2cpg = new HashMap<>();
    private Set<String> sensitive_apis;
    private SymbolizingTool symbolizingTool;

    public Set<XFG> slices = new HashSet<>(); // store all code gadgets of a program
    // backward information of data-deoendence for each statement
    public Map<String, Map<Integer, Set<Integer>>> funcName2backDataInfo = new HashMap<>();
    // forward information of data-deoendence for each statement
    public Map<String, Map<Integer, Set<Integer>>> funcName2forwDataInfo = new HashMap<>();
    // backward information of control-deoendence for each statement
    public Map<String, Map<Integer, Set<Integer>>> funcName2backControlInfo = new HashMap<>();
    // forward information of control-deoendence for each statement
    public Map<String, Map<Integer, Set<Integer>>> funcName2forwControlInfo = new HashMap<>();

    // 将文件名映射
    List<String> files = new ArrayList<>();
    Map<String, Integer> file2Id = new HashMap<>();

    public XFGSliceTool(List<CPG> cpgs, List<String> sensitive_apis, SymbolizingTool symbolizingTool) {
        this.cpgs = cpgs;
        this.sensitive_apis = new HashSet<>(sensitive_apis);
        this.symbolizingTool = symbolizingTool;

        List<CPG> cpgsToBeIgnored = new ArrayList<>();
        for (CPG cpg: cpgs){
            if (funcName2cpg.containsKey(cpg.funcName)){
                cpgsToBeIgnored.add(cpg);
                continue;
            }
            funcName2cpg.put(cpg.funcName, cpg);
            generateForAndBackInfos(cpg);
            if (!files.contains(cpg.fileName))
                files.add(cpg.fileName);
        }

        for (CPG cpg: cpgsToBeIgnored)
            cpgs.remove(cpg);

        for (int i = 0; i < files.size(); ++i)
            file2Id.put(files.get(i), i);
    }

    public void generateForAndBackInfos(CPG cpg){
        Map<Integer, Set<Integer>> backDataInfo = new HashMap<>();
        Map<Integer, Set<Integer>> forwDataInfo = new HashMap<>();
        // forward and backward for data dependence
        for (CodeEdge edge: cpg.getDdgEdges()){
            // backward
            if (!backDataInfo.containsKey(edge.getDestination()))
                backDataInfo.put(edge.getDestination(), new HashSet<>());
            backDataInfo.get(edge.getDestination()).add(edge.getSource());
            // forward
            if (!forwDataInfo.containsKey(edge.getSource()))
                forwDataInfo.put(edge.getSource(), new HashSet<>());
            forwDataInfo.get(edge.getSource()).add(edge.getDestination());
        }

        funcName2backDataInfo.put(cpg.funcName, backDataInfo);
        funcName2forwDataInfo.put(cpg.funcName, forwDataInfo);

        Map<Integer, Set<Integer>> backControlInfo = new HashMap<>();
        Map<Integer, Set<Integer>> forwControlInfo = new HashMap<>();
        // forward and backward for data dependence
        for (CodeEdge edge: cpg.getCdgEdges()){
            // backward
            if (!backControlInfo.containsKey(edge.getDestination()))
                backControlInfo.put(edge.getDestination(), new HashSet<>());
            backControlInfo.get(edge.getDestination()).add(edge.getSource());
            // forward
            if (!forwControlInfo.containsKey(edge.getSource()))
                forwControlInfo.put(edge.getSource(), new HashSet<>());
            forwControlInfo.get(edge.getSource()).add(edge.getDestination());
        }

        funcName2backControlInfo.put(cpg.funcName, backControlInfo);
        funcName2forwControlInfo.put(cpg.funcName, forwControlInfo);
    }

    public void generateSliceForProgram(){
        DWKPoint point = new DWKPoint(sensitive_apis);
        List<CPG> slicesCpg = cpgs.stream().filter(cpg -> cpg.joinSlice).collect(Collectors.toList());
        for (CPG cpg: slicesCpg){
            for (int i = 0; i < cpg.getStatements().size(); ++i) {
                ASTNode stmt = cpg.getStatements().get(i);
                // 是否算感兴趣目标点
                if (!point.judgeSink(stmt))
                    continue;
                Set<Integer> coveredFileIds = new HashSet<>();
                int lineNumber = stmt.getLocation().startLine;
                XFG xfg = new XFG();
                xfg.keyLine = Pair.of(lineNumber, file2Id.get(cpg.fileName));

                List<String> backwardFunctionChain = new ArrayList<>(); // store function call chain in backward slices
                List<Edge<ASTNode>> backwardCDEdges = new ArrayList<>(); // store control dependence edges
                List<Edge<ASTNode>> backwardDDEdges = new ArrayList<>(); // store data dependence edges

                List<ASTNode> backwardLineContents = new ArrayList<>();
                List<Pair<Integer, Integer>> backwardLineInfo = new ArrayList<>();

                List<Integer> backwardIdxs = new ArrayList<>(Arrays.asList(i));
                generateBackwardSlice(cpg.funcName, backwardIdxs, backwardLineContents, backwardFunctionChain,
                        backwardLineInfo, backwardCDEdges, backwardDDEdges, coveredFileIds);


                List<String> forwardFunctionChain = new ArrayList<>(); // store function call chain in backward slices
                List<Edge<ASTNode>> forwardCDEdges = new ArrayList<>(); // store control dependence edges
                List<Edge<ASTNode>> forwardDDEdges = new ArrayList<>(); // store data dependence edges

                List<ASTNode> forwardLineContents = new ArrayList<>();
                List<Pair<Integer, Integer>> forwardLineInfo = new ArrayList<>();

                List<Integer> forwardIdxs = new ArrayList<>(Arrays.asList(i));
                generateForwardSlice(cpg.funcName, forwardIdxs, forwardLineContents, forwardFunctionChain,
                        forwardLineInfo, forwardCDEdges, forwardDDEdges, coveredFileIds);

                int idx = forwardLineContents.indexOf(stmt);
                forwardLineContents.remove(idx);

                forwardLineInfo.remove(idx);
                List<Pair<Integer, Integer>> lines = new ArrayList<>(backwardLineInfo);
                lines.addAll(forwardLineInfo);

                List<ASTNode> contents = new ArrayList<>(backwardLineContents);
                contents.addAll(forwardLineContents);

                List<Pair<Pair<Integer, Integer>, ASTNode>> lineInfos = new ArrayList<>();
                for (int j = 0; j < contents.size(); ++j)
                    lineInfos.add(Pair.of(lines.get(j), contents.get(j)));

                // 先按行号排序，再按文件Id排序
                lineInfos.sort(Comparator.comparing(lineInfo -> lineInfo.first.first));
                lineInfos.sort(Comparator.comparing(lineInfo -> lineInfo.first.second));

                Map<ASTNode, Integer> astNode2idx = new HashMap<>();
                for (int j = 0; j < lineInfos.size(); ++j){
                    Pair<Pair<Integer, Integer>, ASTNode> lineInfo = lineInfos.get(j);
                    xfg.lineNumbers.add(lineInfo.first);
                    xfg.contents.add(symbolizingTool.symbolize(lineInfo.second.getEscapedCodeStr()));
                    xfg.nodes.add(lineInfo.second);
                    astNode2idx.put(lineInfo.second, j);
                }

                Set<CodeEdge> cdEdges = new HashSet<>();
                for (Edge<ASTNode> edge: backwardCDEdges)
                    cdEdges.add(new CodeEdge(astNode2idx.get(edge.getSource()),
                            astNode2idx.get(edge.getDestination()), null));
                for (Edge<ASTNode> edge: forwardCDEdges)
                    cdEdges.add(new CodeEdge(astNode2idx.get(edge.getSource()),
                            astNode2idx.get(edge.getDestination()), null));
                xfg.cdes.addAll(cdEdges);
                xfg.cdes.sort(Comparator.comparing(edge -> edge.getDestination()));
                xfg.cdes.sort(Comparator.comparing(edge -> edge.getSource()));


                Set<CodeEdge> ddEdges = new HashSet<>();
                for (Edge<ASTNode> edge: backwardDDEdges)
                    ddEdges.add(new CodeEdge(astNode2idx.get(edge.getSource()),
                            astNode2idx.get(edge.getDestination()), null));
                for (Edge<ASTNode> edge: forwardDDEdges)
                    ddEdges.add(new CodeEdge(astNode2idx.get(edge.getSource()),
                            astNode2idx.get(edge.getDestination()), null));
                xfg.ddes.addAll(ddEdges);
                xfg.ddes.sort(Comparator.comparing(edge -> edge.getDestination()));
                xfg.ddes.sort(Comparator.comparing(edge -> edge.getSource()));

                for (int fileId: coveredFileIds){
                    String[] paths = files.get(fileId).split(File.pathSeparator);
                    String fileN = paths[paths.length - 1];
                    xfg.id2file.put(fileId, fileN);
                }


                slices.add(xfg);
            }
        }
    }

    public void generateBackwardSlice(String funcName, List<Integer> sliceIdxs, List<ASTNode> slices, List<String> functionChain,
                                      List<Pair<Integer, Integer>> sliceLines, List<Edge<ASTNode>> cdEdges, List<Edge<ASTNode>> ddEdges,
                                      Set<Integer> coveredFileIds){
        if (functionChain.contains(funcName))
            return;
        // sliceIdxs stores all indexes of nodes of slices
        CPG cpg = funcName2cpg.get(funcName);
        functionChain.add(funcName);
        // computes all nodes with program-dependence with startIdx in a single function first
        Map<Integer, Set<Integer>> dataInfo = funcName2backDataInfo.get(funcName);
        Map<Integer, Set<Integer>> controlInfo = funcName2backControlInfo.get(funcName);
        Queue<Integer> workList = new LinkedList<>(sliceIdxs);
        while (!workList.isEmpty()){
            int curIdx = workList.poll();
            // data dependence
            for (int o: dataInfo.getOrDefault(curIdx, Set.of())){
                Edge<ASTNode> edge = new Edge(cpg.getStatements().get(o), cpg.getStatements().get(curIdx)) {
                    @Override
                    public Map<String, Object> getProperties() {
                        return null;
                    }
                };

                if (!ddEdges.contains(edge))
                    ddEdges.add(edge);
                if (!sliceIdxs.contains(o)){
                    sliceIdxs.add(o);
                    workList.add(o);
                }
            }

            // control dependence
            for (int o: controlInfo.getOrDefault(curIdx, Set.of())){
                Edge<ASTNode> edge = new Edge(cpg.getStatements().get(o), cpg.getStatements().get(curIdx)) {
                    @Override
                    public Map<String, Object> getProperties() {
                        return null;
                    }
                };

                if (!cdEdges.contains(edge))
                    cdEdges.add(edge);
                if (!sliceIdxs.contains(o)){
                    sliceIdxs.add(o);
                    workList.add(o);
                }
            }
        }

        coveredFileIds.add(file2Id.get(cpg.fileName));
        Collections.sort(sliceIdxs);

        for (int id: sliceIdxs){
            ASTNode stmt = cpg.getStatements().get(id);
            // 添加slice行代码
            slices.add(stmt);
            // 添加slice行行号和文件id
            sliceLines.add(Pair.of(stmt.getLocation().startLine, file2Id.get(cpg.fileName)));

            CallExprTool callExprTool = new CallExprTool();
            callExprTool.judgeCall(stmt);
            if (callExprTool.functionName != null && funcName2cpg.containsKey(callExprTool.functionName)){
                CPG otherCPG = funcName2cpg.get(callExprTool.functionName);
                // 以前面一行代码的return语句为起点反向遍历
                if (!(otherCPG.getStatements().get(otherCPG.getStatements().size() - 1) instanceof ReturnStatement))
                    continue;
                List<Integer> newStartIdxs = new ArrayList<>(Arrays.asList(otherCPG.getStatements().size() - 1));
                ddEdges.add(new Edge<ASTNode>(otherCPG.getStatements().get(otherCPG.getStatements().size() - 1), stmt) {
                    @Override
                    public Map<String, Object> getProperties() {
                        return null;
                    }
                });
                generateBackwardSlice(otherCPG.funcName, newStartIdxs, slices, functionChain, sliceLines, cdEdges,
                        ddEdges, coveredFileIds);
            }
        }
    }


    public void generateForwardSlice(String funcName, List<Integer> sliceIdxs, List<ASTNode> slices, List<String> functionChain,
                                     List<Pair<Integer, Integer>> sliceLines, List<Edge<ASTNode>> cdEdges, List<Edge<ASTNode>> ddEdges,
                                     Set<Integer> coveredFileIds){
        if (functionChain.contains(funcName))
            return;
        // sliceIdxs stores all indexes of nodes of slices
        CPG cpg = funcName2cpg.get(funcName);
        functionChain.add(funcName);
        // computes all nodes with program-dependence with startIdx in a single function first
        Map<Integer, Set<Integer>> dataInfo = funcName2forwDataInfo.get(funcName);
        Map<Integer, Set<Integer>> controlInfo = funcName2forwControlInfo.get(funcName);
        Queue<Integer> workList = new LinkedList<>(sliceIdxs);

        while (!workList.isEmpty()){
            int curIdx = workList.poll();
            // data dependence
            for (int o: dataInfo.getOrDefault(curIdx, Set.of())){
                Edge<ASTNode> edge = new Edge(cpg.getStatements().get(curIdx), cpg.getStatements().get(o)) {
                    @Override
                    public Map<String, Object> getProperties() {
                        return null;
                    }
                };

                if (!ddEdges.contains(edge))
                    ddEdges.add(edge);
                if (!sliceIdxs.contains(o)){
                    sliceIdxs.add(o);
                    workList.add(o);
                }
            }

            // control dependence
            for (int o: controlInfo.getOrDefault(curIdx, Set.of())){
                Edge<ASTNode> edge = new Edge(cpg.getStatements().get(curIdx), cpg.getStatements().get(o)) {
                    @Override
                    public Map<String, Object> getProperties() {
                        return null;
                    }
                };

                if (!cdEdges.contains(edge))
                    cdEdges.add(edge);
                if (!sliceIdxs.contains(o)){
                    sliceIdxs.add(o);
                    workList.add(o);
                }
            }
        }

        coveredFileIds.add(file2Id.get(cpg.fileName));
        Collections.sort(sliceIdxs);

        for (int id: sliceIdxs){
            ASTNode stmt = cpg.getStatements().get(id);
            // 添加slice行代码
            slices.add(stmt);
            // 添加slice行行号和文件id
            sliceLines.add(Pair.of(stmt.getLocation().startLine, file2Id.get(cpg.fileName)));

            CallExprTool callExprTool = new CallExprTool();
            callExprTool.judgeCall(stmt);
            if (callExprTool.functionName != null && funcName2cpg.containsKey(callExprTool.functionName)){
                CPG otherCPG = funcName2cpg.get(callExprTool.functionName);
                // 以前面一行代码的return语句为起点反向遍历
                assert callExprTool.argNum > 0;
                List<Integer> newStartIdxs = new ArrayList<>();
                for (int i = 0; i < callExprTool.argNum; ++i){
                    newStartIdxs.add(i);
                    ddEdges.add(new Edge<ASTNode>(stmt, otherCPG.getStatements().get(i)) {
                        @Override
                        public Map<String, Object> getProperties() {
                            return null;
                        }
                    });
                }

                generateForwardSlice(otherCPG.funcName, newStartIdxs, slices, functionChain, sliceLines, cdEdges,
                        ddEdges, coveredFileIds);
            }
        }
    }
}
