import cdg.CDGCreator;
import cfg.ASTToCFGConverter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import cpg.CPG;
import cpg.FileParseUtil;
import cpg.ToJsonUtil;
import ddg.CFGAndUDGToDefUseCFG;
import ddg.DDGCreator;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import udg.useDefAnalysis.ASTDefUseAnalyzer;
import udg.useDefAnalysis.CalleeInfos;
import udg.useDefGraph.CFGToUDGConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class generateFileTest {

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

        String root_path = "D:/projects/python/vul detect/datasets/Juliet/C/testcases";
        List<String> CWE_paths = Arrays.asList("CWE121", "CWE122", "CWE123", "CWE124", "CWE126", "CWE127");
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
                List<CPG> cpgs = fileParseUtil.fileParsing(fileTemp.getAbsolutePath());

                for (CPG cpg: cpgs){
                    if (cpg.funcName.equals("main") || cpg.getDdgEdges().size() <= 0 || cpg.getStatements().size() <= 5)
                        continue;
                    cpg.fileName = fileTemp.getName();
                    Map<String, Object> jsonCpg = ToJsonUtil.prettyJsonCPG(ToJsonUtil.transformCPG(cpg));
                    totalJsonCpgs.add(jsonCpg);
                }
            }
            System.out.println("====================================");
        }

        File outputFile = new File("D:/projects/python/vul_explainers/datasets/buffer_overflow/all.json");
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
}
