package cpg;

import ast.ASTNode;
import cdg.CDG;
import cdg.CDGEdge;
import cfg.CFG;
import cfg.CFGEdge;
import cfg.nodes.*;
import ddg.DataDependenceGraph.DDG;


import java.util.*;
import java.util.stream.Collectors;

// Defining Code Property Graph
public class CPG {
    private List<ASTNode> statements = new ArrayList<>();
    private Map<ASTNode, Integer> statement2Idx = new HashMap<>();

    private List<CodeEdge> cfgEdges = new ArrayList<>();
    private List<CodeEdge> cdgEdges = new ArrayList<>();
    private List<CodeEdge> ddgEdges = new ArrayList<>();

    public String funcName; // 函数名
    public String fileName; // 文件名
    public boolean joinSlice = true; // slice的时候是否考虑

    public void initCFGEdges(CFG cfg){
        // 筛选出CFGNode中ASTNodeContainer类型
        List<ASTNodeContainer> nodes = cfg.getVertices().stream().
                filter(cfgNode -> cfgNode instanceof ASTNodeContainer).
                map(node -> (ASTNodeContainer) node).
                sorted().
                collect(Collectors.toList());


        statements.addAll(nodes.stream().map(ASTNodeContainer::getASTNode).collect(Collectors.toList()));
        for (int i = 0; i < statements.size(); ++i)
            statement2Idx.put(statements.get(i), i);

        List<CFGEdge> edges = cfg.getEdges().stream().filter(
                cfgEdge -> !((cfgEdge.getSource() instanceof CFGEntryNode) ||
                        (cfgEdge.getDestination() instanceof CFGExitNode))).
                collect(Collectors.toList());

        cfgEdges.addAll(edges.stream().map(edge -> {
            int sourceId = statement2Idx.get(((ASTNodeContainer)edge.getSource()).getASTNode());
            int dstId = statement2Idx.get(((ASTNodeContainer)edge.getDestination()).getASTNode());
            return new CodeEdge(sourceId, dstId, edge.getLabel());
        }).collect(Collectors.toList()));
    }


    public void initCDGEdges(CDG cdg){
        List<CDGEdge> edges = cdg.getEdges();
        cdgEdges.addAll(edges.stream().map(edge -> {
            int sourceId = statement2Idx.get(((ASTNodeContainer)edge.getSource()).getASTNode());
            int dstId = statement2Idx.get(((ASTNodeContainer)edge.getDestination()).getASTNode());
            return new CodeEdge(sourceId, dstId, null);
        }).collect(Collectors.toList()));
    }


    public void initDDGEdges(DDG ddg){
        ddgEdges.addAll(ddg.getDefUseEdges().stream().map(edge -> {
            int sourceId = statement2Idx.get(edge.src);
            int dstId = statement2Idx.get(edge.dst);
            return new CodeEdge(sourceId, dstId, edge.symbol);
        }).collect(Collectors.toList()));
    }

    public List<ASTNode> getStatements() {
        return statements;
    }

    public Map<ASTNode, Integer> getStatement2Idx() {
        return statement2Idx;
    }

    public List<CodeEdge> getCfgEdges() {
        return cfgEdges;
    }

    public List<CodeEdge> getCdgEdges() {
        return cdgEdges;
    }

    public List<CodeEdge> getDdgEdges() {
        return ddgEdges;
    }
}
