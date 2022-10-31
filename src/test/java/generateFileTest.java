import io.analyzer.mainTool.antlr.Cpp.CPP14Lexer;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.ast.declaration.ClassDefStatement;
import io.analyzer.mainTool.ast.functionDef.FunctionDef;
import io.analyzer.mainTool.cdg.CDGCreator;
import io.analyzer.mainTool.cfg.ASTToCFGConverter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.analyzer.mainTool.cpg.CPG;
import io.analyzer.mainTool.cpg.FileParseUtil;
import io.analyzer.mainTool.cpg.ToJsonUtil;
import io.analyzer.mainTool.ddg.CFGAndUDGToDefUseCFG;
import io.analyzer.mainTool.ddg.DDGCreator;
import io.analyzer.mainTool.parsing.CPP.builder.FileBuilder;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import io.analyzer.mainTool.udg.useDefAnalysis.ASTDefUseAnalyzer;
import io.analyzer.mainTool.udg.useDefAnalysis.CalleeInfos;
import io.analyzer.mainTool.udg.useDefGraph.CFGToUDGConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class generateFileTest {
    private String root_path = "D:/projects/python/vul detect/datasets/Juliet/C/testcases";
    private List<String> CWE_paths = Arrays.asList("CWE23", "CWE36");
    private String outputFileName = "D:/projects/python/vul_explainers/datasets/path traversal/all.json";
    private String outputPretrainName = "D:/projects/python/vul_explainers/datasets/path traversal/pretrainCorpus.json";

    public void findFileNames(File dir, List<File> files){
        //如果是目录的情况
        //循环遍历files
        for (File fileTemp : Objects.requireNonNull(dir.listFiles())){
            //子级是目录
            if (fileTemp.isDirectory())
                //递归再次进行判断
                findFileNames(fileTemp, files);
            else
                //子级是文件
                files.add(fileTemp);
        }
    }


    @Test
    public void test() throws IOException {
        long startTime = System.currentTimeMillis();
        String calleeInfoFile = "src/main/resources/calleeInfos.json";
        File file = new File(calleeInfoFile);
        String file1 = FileUtils.readFileToString(file);//前面两行是读取文件
        JSONObject jsonobject = JSON.parseObject(file1);

        ASTToCFGConverter cfgConverter = new ASTToCFGConverter();
        CFGToUDGConverter converter = new CFGToUDGConverter();
        ASTDefUseAnalyzer astAnalyzer = new ASTDefUseAnalyzer();
        CalleeInfos calleeInfos = new CalleeInfos();
        converter.setAstAnalyzer(astAnalyzer);

        CFGAndUDGToDefUseCFG defUseConverter = new CFGAndUDGToDefUseCFG();
        DDGCreator ddgCreator = new DDGCreator();
        CDGCreator cdgCreator = new CDGCreator();

        // argDef
        Map<String, List<Integer>> argDefInfos = (Map<String, List<Integer>>) jsonobject.get("ArgDefs");
        for (Map.Entry<String, List<Integer>> entry: argDefInfos.entrySet()){
            String funcName = entry.getKey();
            List<Integer> argNs = entry.getValue();
            for (int argN : argNs)
                calleeInfos.addArgDef(funcName, argN);
        }

        Map<String, List<Integer>> argUseInfos = (Map<String, List<Integer>>) jsonobject.get("ArgUses");
        for (Map.Entry<String, List<Integer>> entry: argUseInfos.entrySet()){
            String funcName = entry.getKey();
            List<Integer> argNs = entry.getValue();
            for (int argN : argNs)
                calleeInfos.addArgUse(funcName, argN);
        }

        Map<String, Integer> argDefStartIds = (Map<String, Integer>) jsonobject.get("ArgDefStartIds");
        for (Map.Entry<String,Integer> entry: argDefStartIds.entrySet()){
            String funcName = entry.getKey();
            int argN = entry.getValue();
            calleeInfos.addArgDefStartIds(funcName, argN);
        }

        int fileCount = 0;
        File rootDir = new File(root_path);

        List<Map<String, Object>> totalJsonCpgs = new ArrayList<>();

        for (File subDir: Objects.requireNonNull(rootDir.listFiles())){
            // System.out.println(subDir.getName());
            String cweName = subDir.getName().split("_")[0];
            if (!CWE_paths.contains(cweName))
                continue;
            List<File> files = new ArrayList<>();
            findFileNames(subDir, files);
            files = files.stream().filter(f -> !(f.getName().startsWith("main") || f.getName().startsWith("testcases") ||
                    f.getName().toLowerCase().startsWith("makefile") || f.getName().endsWith("bat"))).
                    collect(Collectors.toList());

            fileCount += files.size();
            for (int i = 0; i < files.size(); ++i){
                File fileTemp = files.get(i);
                System.out.printf("%d / %d - %s%n", i, files.size(), fileTemp.getName());
                ddgCreator.clear();
                FileParseUtil fileParseUtil = new FileParseUtil(cfgConverter, converter, defUseConverter, ddgCreator, cdgCreator);
                try{
                    List<CPG> cpgs = fileParseUtil.fileParsing(fileTemp.getAbsolutePath());
                    for (CPG cpg: cpgs){
                        if (cpg.funcName.equals("main") || cpg.getDdgEdges().size() <= 0 || cpg.getStatements().size() <= 5)
                            continue;
                        cpg.fileName = fileTemp.getName();
                        Map<String, Object> jsonCpg = ToJsonUtil.prettyJsonCPG(ToJsonUtil.transformCPG(cpg));
                        totalJsonCpgs.add(jsonCpg);
                    }
                }catch(Exception e){
                    System.out.printf("syntax error might appear in file %s", fileTemp.getName());
                    continue;
                }

            }
            System.out.println("====================================");
        }

        File outputFile = new File(outputFileName);
        Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
        JSON.writeJSONString(writer, totalJsonCpgs, SerializerFeature.PrettyFormat);
        long endTime = System.currentTimeMillis();
        long spentTime = (endTime - startTime) / 1000;
        System.out.println(fileCount);
        System.out.println(totalJsonCpgs.size());
        System.out.printf("spent time: %d hours - %d minutes - %d seconds", spentTime / 3600,
                (spentTime % 3600) / 60, (spentTime % 3600) % 60);
        return;
    }

    @Test
    public void generatePreTrainCorpus() throws IOException {
        long startTime = System.currentTimeMillis();
        File rootDir = new File(root_path);
        Set<String> pretrainCorpus = new HashSet<>();

        for (File subDir: Objects.requireNonNull(rootDir.listFiles())){
            // System.out.println(subDir.getName());
            String cweName = subDir.getName().split("_")[0];
            if (!CWE_paths.contains(cweName))
                continue;
            List<File> files = new ArrayList<>();
            findFileNames(subDir, files);
            files = files.stream().filter(f -> !(f.getName().startsWith("main") || f.getName().startsWith("testcases") ||
                            f.getName().toLowerCase().startsWith("makefile") || f.getName().endsWith("bat"))).
                    collect(Collectors.toList());

            for (int i = 0; i < files.size(); ++i){
                File fileTemp = files.get(i);
                System.out.printf("%d / %d - %s%n", i, files.size(), fileTemp.getName());

                ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(fileTemp));
                CPP14Lexer lexer = new CPP14Lexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                CPP14Parser parser = new CPP14Parser(tokens);
                ParseTree tree = parser.translationunit();
                FileBuilder builder = new FileBuilder();
                ParseTreeWalker walker = new ParseTreeWalker();
                try{
                    walker.walk(builder, tree);
                }catch (Exception e){
                    System.out.printf("syntax error might appear in file %s", fileTemp.getName());
                    continue;
                }


                List<FunctionDef> functions = new ArrayList<>();
                functions.addAll(builder.functionDefs);
                for (ClassDefStatement classDecl: builder.classDefs)
                    functions.addAll(classDecl.functionDefs);

                for (FunctionDef functionDef: functions)
                    pretrainCorpus.add(functionDef.getContent().getEscapedCodeStr());
            }
            System.out.println("====================================");
        }

        File outputFile = new File(outputPretrainName);
        Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
        JSON.writeJSONString(writer, pretrainCorpus, SerializerFeature.PrettyFormat);
        long endTime = System.currentTimeMillis();
        long spentTime = (endTime - startTime) / 1000;

        System.out.printf("spent time: %d hours - %d minutes - %d seconds", spentTime / 3600,
                (spentTime % 3600) / 60, (spentTime % 3600) % 60);
        return;
    }
}
