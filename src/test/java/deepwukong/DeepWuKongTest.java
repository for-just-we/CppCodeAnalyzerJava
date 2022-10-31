package deepwukong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.analyzer.extraTools.deepwukong.XFG;
import io.analyzer.extraTools.deepwukong.XFGSliceTool;
import io.analyzer.extraTools.utils.SymbolizingTool;
import io.analyzer.mainTool.cdg.CDGCreator;
import io.analyzer.mainTool.cfg.ASTToCFGConverter;
import io.analyzer.mainTool.cpg.CPG;
import io.analyzer.mainTool.cpg.FileParseUtil;
import io.analyzer.mainTool.cpg.ToJsonUtil;
import io.analyzer.mainTool.ddg.CFGAndUDGToDefUseCFG;
import io.analyzer.mainTool.ddg.DDGCreator;
import io.analyzer.mainTool.udg.useDefAnalysis.ASTDefUseAnalyzer;
import io.analyzer.mainTool.udg.useDefAnalysis.CalleeInfos;
import io.analyzer.mainTool.udg.useDefGraph.CFGToUDGConverter;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DeepWuKongTest {
    private String root_path = "D:/projects/python/vul detect/datasets/Juliet/C/testcases";
    private List<String> CWE_paths = Arrays.asList("CWE121", "CWE122", "CWE123", "CWE124", "CWE126", "CWE127" );
    private String outputFileName = "D:/projects/python/vul_explainers/datasets/buffer overflow/all_xfg.json";

    Pattern pattern = Pattern.compile("\\d+");

    public boolean isSameProject(int projectId, String fileName){
        Matcher matcher = pattern.matcher(fileName);
        int count = 0, value = -1;
        String item;
        while (matcher.find()){
            item = matcher.group();
            if (count == 2)
                value = Integer.parseInt(item);
            count += 1;
        }
        return projectId == value;
    }

    @Test
    public void match(){

    }

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

        String sensitive_api_file = "src/main/resources/sensiAPI.txt";
        FileInputStream fileInputStream = new FileInputStream(new File(sensitive_api_file));
        byte[] contents = fileInputStream.readAllBytes();
        String apis = new String(contents);
        List<String> sensiAPIs = List.of(apis.split(","));

        Set<String> sysDefinedVars = Set.of("argc", "argv", "stdin", "stdout", "cin", "cout", "SOCKET_ERROR", "NULL", "nullptr");
        SymbolizingTool symbolizingTool = new SymbolizingTool(sysDefinedVars, new HashSet<>(sensiAPIs));

        List<String> fileNames = Arrays.asList(
                "D:\\projects\\python\\vul detect\\datasets\\Juliet\\C\\testcases\\CWE121_Stack_Based_Buffer_Overflow\\s01\\CWE121_Stack_Based_Buffer_Overflow__CWE129_connect_socket_83.h",
                "D:\\projects\\python\\vul detect\\datasets\\Juliet\\C\\testcases\\CWE121_Stack_Based_Buffer_Overflow\\s01\\CWE121_Stack_Based_Buffer_Overflow__CWE129_connect_socket_83_bad.cpp",
                "D:\\projects\\python\\vul detect\\datasets\\Juliet\\C\\testcases\\CWE121_Stack_Based_Buffer_Overflow\\s01\\CWE121_Stack_Based_Buffer_Overflow__CWE129_connect_socket_83_goodB2G.cpp",
                "D:\\projects\\python\\vul detect\\datasets\\Juliet\\C\\testcases\\CWE121_Stack_Based_Buffer_Overflow\\s01\\CWE121_Stack_Based_Buffer_Overflow__CWE129_connect_socket_83_goodG2B.cpp",
                "D:\\projects\\python\\vul detect\\datasets\\Juliet\\C\\testcases\\CWE121_Stack_Based_Buffer_Overflow\\s01\\CWE121_Stack_Based_Buffer_Overflow__CWE129_connect_socket_83a.cpp"
        );
        String ioFileName = "src/test/testfiles/sard_test_cases/io.c";
        ddgCreator.clear();
        FileParseUtil fileParseUtil = new FileParseUtil(cfgConverter, converter, defUseConverter, ddgCreator, cdgCreator);
        List<CPG> cpgs = fileParseUtil.fileParsing(ioFileName);
        for (CPG cpg: cpgs)
            cpg.joinSlice = false;
        for (String fileName: fileNames)
            cpgs.addAll(fileParseUtil.fileParsing(fileName));

        symbolizingTool.clear();
        symbolizingTool.getVarFuncNamesInFile(cpgs);
        XFGSliceTool xfgSliceTool = new XFGSliceTool(cpgs, sensiAPIs, symbolizingTool);
        xfgSliceTool.generateSliceForProgram();
        xfgSliceTool.slices = xfgSliceTool.slices.stream().filter(xfg -> xfg.ddes.size() > 0).collect(Collectors.toSet());
        List<Map<String, Object>> jsonXFG = xfgSliceTool.slices.stream().map(XFG::toJson).collect(Collectors.toList());
        return;
    }

    @Test
    public void generateAllXFGs() throws IOException {
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

        String sensitive_api_file = "src/main/resources/sensiAPI.txt";
        FileInputStream fileInputStream = new FileInputStream(new File(sensitive_api_file));
        byte[] contents = fileInputStream.readAllBytes();
        String apis = new String(contents);
        List<String> sensiAPIs = List.of(apis.split(","));

        int fileCount = 0;
        Set<String> sysDefinedVars = Set.of("argc", "argv", "stdin", "stdout", "cin", "cout", "SOCKET_ERROR", "NULL", "nullptr");

        String ioFileName = "src/test/testfiles/sard_test_cases/io.c";
        FileParseUtil fileParseUtil = new FileParseUtil(cfgConverter, converter, defUseConverter, ddgCreator, cdgCreator);
        List<CPG> ioCpgs = fileParseUtil.fileParsing(ioFileName);
        for (CPG cpg: ioCpgs)
            cpg.joinSlice = false;

        List<Map<String, Object>> totalJsonXfgs = new ArrayList<>();
        SymbolizingTool symbolizingTool = new SymbolizingTool(sysDefinedVars, new HashSet<>(sensiAPIs));
        File rootDir = new File(root_path);
        for (File subDir: Objects.requireNonNull(rootDir.listFiles())) {
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
            int i = 0;
            int cur;
            while (i < files.size()){
                File fileTemp = files.get(i);
                System.out.printf("%d / %d - %s%n", i, files.size(), fileTemp.getName());
                ddgCreator.clear();
                cur = i;
                String fileName = fileTemp.getName();

                Matcher matcher = pattern.matcher(fileName);
                int count = 0, projectId = -2;
                String item;
                while (matcher.find()){
                    item = matcher.group();
                    if (count == 2)
                        projectId = Integer.parseInt(item);
                    count += 1;
                }

                ++i;
                while (i < files.size() && isSameProject(projectId, files.get(i).getName()))
                    ++i;
                if (i > cur)
                    --i;

                List<CPG> cpgs = new ArrayList<>(ioCpgs);
                try{
                    while (cur <= i){
                        cpgs.addAll(fileParseUtil.fileParsing(files.get(cur).getAbsolutePath()));
                        ++cur;
                    }
                    ++i;
                }catch (Exception e){
                    System.out.printf("syntax error might appear in file %s", fileTemp.getName());
                    ++i;
                    continue;
                }

                symbolizingTool.clear();
                symbolizingTool.getVarFuncNamesInFile(cpgs);
                XFGSliceTool xfgSliceTool = new XFGSliceTool(cpgs, sensiAPIs, symbolizingTool);
                xfgSliceTool.generateSliceForProgram();
                xfgSliceTool.slices = xfgSliceTool.slices.stream().filter(xfg -> xfg.ddes.size() > 0).collect(Collectors.toSet());
                List<Map<String, Object>> jsonXFG = xfgSliceTool.slices.stream().map(XFG::toJson).collect(Collectors.toList());


                totalJsonXfgs.addAll(jsonXFG.stream().map(ToJsonUtil::prettyJsonXFG).collect(Collectors.toList()));
            }
        }

        File outputFile = new File(outputFileName);
        Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
        JSON.writeJSONString(writer, totalJsonXfgs, SerializerFeature.PrettyFormat);
        long endTime = System.currentTimeMillis();
        long spentTime = (endTime - startTime) / 1000;
        System.out.println(fileCount);
        System.out.println(totalJsonXfgs.size());
        System.out.printf("spent time: %d hours - %d minutes - %d seconds", spentTime / 3600,
                (spentTime % 3600) / 60, (spentTime % 3600) % 60);
        return;
    }

    @Test
    public void processDataset() throws IOException {
        File outputFile = new File(outputFileName);
        String file1 = FileUtils.readFileToString(outputFile);//前面两行是读取文件
        JSONObject jsonobject = JSON.parseObject(file1);

        return;
    }
}
