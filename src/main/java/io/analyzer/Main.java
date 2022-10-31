package io.analyzer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

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
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.System.exit;

public class Main {
    // cxx后缀名
    public static Set<String> cppSuffixs = Set.of("c", "cxx", "cpp", "cc",
            "cp", "c++", "h", "hpp", "hxx");

    // 递归获取文件名
    public static void listFile(File dir, List<String> fileList){
        for (File fileItem : dir.listFiles()){
            if (fileItem.isDirectory())
                listFile(fileItem, fileList);
            else {
                String fileName = fileItem.getName();
                String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
                if (cppSuffixs.contains(suffix))
                    fileList.add(fileItem.getAbsolutePath());
            }
        }
    }

    // 将程序解析为CPG，并dump出json格式
    public static void main(String[] args) throws IOException, ParseException {
        Options options = new Options();
        options.addOption("h", "help", false, "print options' information");
        options.addOption("f", "file", true, "path of c source file to be analyzed");
        options.addOption("d", "dir", true, "dir which contains all c source files to be analyzed");
        options.addOption("o", "output",true, "output file which contains parsing results, default result file will be in the same dir as parsing file or dir");
        options.addOption("c", "callee-info", true, "specify json file path containing callee info, default value is 'src/main/resources/calleeInfos.json'");

        // 创建命令行解析器
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if(commandLine.hasOption("h")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("Options", options);
        }

        if (!commandLine.hasOption("f") && !commandLine.hasOption("d")) {
            System.err.println("please specify a source file or source dir.");
            exit(-1);
        }
        else if (commandLine.hasOption("f") && commandLine.hasOption("d")) {
            System.err.println("please do not specify source file and source dir in the same time");
            exit(-1);
        }

        long startTime = System.currentTimeMillis();
        String calleeInfoFile;
        if (!commandLine.hasOption("c"))
            calleeInfoFile  = "src/main/resources/calleeInfos.json";
        else
            calleeInfoFile = commandLine.getOptionValue("c");
        File file = new File(calleeInfoFile);
        String calleeInfoContent = FileUtils.readFileToString(file);//前面两行是读取文件
        JSONObject jsonobject = JSON.parseObject(calleeInfoContent);

        ASTToCFGConverter cfgConverter = new ASTToCFGConverter();
        CFGToUDGConverter converter = new CFGToUDGConverter();
        ASTDefUseAnalyzer astAnalyzer = new ASTDefUseAnalyzer();
        CalleeInfos calleeInfos = new CalleeInfos();
        converter.setAstAnalyzer(astAnalyzer);

        CFGAndUDGToDefUseCFG defUseConverter = new CFGAndUDGToDefUseCFG();
        DDGCreator ddgCreator = new DDGCreator();
        CDGCreator cdgCreator = new CDGCreator();

        // 加载callee infos
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

        // 加载待处理文件
        String potentialOutputFileName ="";
        List<String> fileToParse = new ArrayList<>();
        if (commandLine.hasOption("f")) {
            String fileName = commandLine.getOptionValue("f");
            fileToParse.add(fileName);
            String filePath = fileName.substring(0, fileName.lastIndexOf(File.separator));
            potentialOutputFileName = filePath + File.separator + "result.json";
        }
        else if (commandLine.hasOption("d")) {
            listFile(new File(commandLine.getOptionValue("d")), fileToParse);
            potentialOutputFileName = commandLine.getOptionValue("d") + File.separator + "result.json";
        }

        // 如果没有待解析文件
        if (fileToParse.size() == 0){
            System.out.println("Specified dir should at least contain one cpp file");
            exit(-1);
        }
        // 输出文件路径
        String outputFileName;
        if (commandLine.hasOption("o"))
            outputFileName = commandLine.getOptionValue("o");
        else
            outputFileName = potentialOutputFileName;

        // 解析开始
        int successfulCount = 0;
        List<Map<String, Object>> totalJsonCpgs = new ArrayList<>();
        FileParseUtil fileParseUtil = new FileParseUtil(cfgConverter, converter, defUseConverter, ddgCreator, cdgCreator);
        for (int i = 0; i < fileToParse.size(); ++i){
            fileParseUtil.clear();
            String fileName = fileToParse.get(i);
            System.out.printf("%d / %d - %s%n", i, fileToParse.size(), fileName);
            try{
                List<CPG> cpgs = fileParseUtil.fileParsing(fileName);
                for (CPG cpg: cpgs){
                    cpg.fileName = fileName;
                    Map<String, Object> jsonCpg = ToJsonUtil.prettyJsonCPG(ToJsonUtil.transformCPG(cpg));
                    totalJsonCpgs.add(jsonCpg);
                }
                successfulCount += 1;
            }catch(Exception e){
                System.out.printf("syntax error might appear in file %s", fileName);
                continue;
            }
            System.out.println("====================================");
        }

        File outputFile = new File(outputFileName);
        Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
        JSON.writeJSONString(writer, totalJsonCpgs, SerializerFeature.PrettyFormat);
        long endTime = System.currentTimeMillis();
        long spentTime = (endTime - startTime) / 1000;
        System.out.printf("result is placed in %s%n", outputFileName);
        System.out.printf("%d files have been successfully parsed%n", successfulCount);
        System.out.printf("%d total files should have been successfully parsed%n", fileToParse.size());
        System.out.println(totalJsonCpgs.size());
        System.out.printf("spent time: %d hours - %d minutes - %d seconds", spentTime / 3600,
                (spentTime % 3600) / 60, (spentTime % 3600) % 60);
    }
}
