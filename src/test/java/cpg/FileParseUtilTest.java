package cpg;

import cdg.CDGCreator;
import cfg.ASTToCFGConverter;
import ddg.CFGAndUDGToDefUseCFG;
import ddg.DDGCreator;
import org.junit.Test;
import udg.useDefAnalysis.ASTDefUseAnalyzer;
import udg.useDefAnalysis.CalleeInfos;
import udg.useDefGraph.CFGToUDGConverter;

import java.io.IOException;
import java.util.List;

public class FileParseUtilTest {
    @Test
    public void test() throws IOException {
        long startTime = System.currentTimeMillis();
        String fileName = "src/test/testfiles/sard_test_cases/CWE_119_122_switch.c";
        ASTToCFGConverter cfgConverter = new ASTToCFGConverter();
        CFGToUDGConverter converter = new CFGToUDGConverter();
        ASTDefUseAnalyzer astAnalyzer = new ASTDefUseAnalyzer();
        CalleeInfos calleeInfos = new CalleeInfos();

        {
            calleeInfos.addArgDef("memcpy", 0);
            calleeInfos.addArgUse("memcpy", 1);
            calleeInfos.addArgDef("memmove", 0);
            calleeInfos.addArgUse("memmove", 1);
            calleeInfos.addArgDef("memset", 0);
            calleeInfos.addArgDef("fgets", 0);
            calleeInfos.addArgDef("recv", 1);

            calleeInfos.addArgDefStartIds("scanf", 1);
            astAnalyzer.setCalleeInfos(calleeInfos);
        }
        converter.setAstAnalyzer(astAnalyzer);

        CFGAndUDGToDefUseCFG defUseConverter = new CFGAndUDGToDefUseCFG();
        DDGCreator ddgCreator = new DDGCreator();
        CDGCreator cdgCreator = new CDGCreator();

        FileParseUtil fileParseUtil = new FileParseUtil(cfgConverter, converter, defUseConverter, ddgCreator, cdgCreator);
        List<CPG> cpgs = fileParseUtil.fileParsing(fileName);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        return;
    }
}