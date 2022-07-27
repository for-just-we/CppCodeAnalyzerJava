package cpg;

import antlr.Cpp.CPP14Lexer;
import antlr.Cpp.CPP14Parser;
import ast.functionDef.FunctionDef;
import cdg.CDG;
import cdg.CDGCreator;
import cfg.ASTToCFGConverter;
import cfg.CFG;
import ddg.CFGAndUDGToDefUseCFG;
import ddg.DDGCreator;
import ddg.DataDependenceGraph.DDG;
import ddg.DefUseCFG.DefUseCFG;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import parsing.CPP.builder.FunctionDefBuilder;
import udg.useDefAnalysis.ASTDefUseAnalyzer;
import udg.useDefAnalysis.CalleeInfos;
import udg.useDefGraph.CFGToUDGConverter;
import udg.useDefGraph.UseDefGraph;

public class CPGTest {
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

    @Test
    public void test(){
        long startTime = System.currentTimeMillis();
        String code = "static void goodG2B2(int a)\n" +
                "{\n" +
                "    char * data;\n" +
                "    data = NULL;\n" +
                "    switch(6)\n" +
                "    {\n" +
                "    case 6:\n" +
                "        /* FIX: Allocate and point data to a large buffer that is at least as large as the large buffer used in the sink */\n" +
                "        data = (char *)malloc(100*sizeof(char));\n" +
                "        if (data == NULL) {exit(-1);}\n" +
                "        data[0] = '\\0'; /* null terminate */\n" +
                "        break;\n" +
                "    default:\n" +
                "        /* INCIDENTAL: CWE 561 Dead Code, the code below will never run */\n" +
                "        printLine(\"Benign, fixed string\");\n" +
                "        break;\n" +
                "    }\n" +
                "    {\n" +
                "        size_t i;\n" +
                "        char source[100];\n" +
                "        memset(source, 'C', 100-1); /* fill with 'C's */\n" +
                "        source[100-1] = '\\0'; /* null terminate */\n" +
                "        /* POTENTIAL FLAW: Possible buffer overflow if source is larger than data */\n" +
                "        for (i = 0; i < 100; i++)\n" +
                "        {\n" +
                "            data[i] = source[i];\n" +
                "        }\n" +
                "        data[100-1] = '\\0'; /* Ensure the destination buffer is null terminated */\n" +
                "        printLine(data);\n" +
                "        free(data);\n" +
                "    }\n" +
                "}";

        // AST
        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.functiondefinition();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionDefBuilder functionDefBuilder = new FunctionDefBuilder();
        walker.walk(functionDefBuilder, tree);

        // CFG
        FunctionDef functionDef = functionDefBuilder.functionDef;
        ASTToCFGConverter cfgConverter = new ASTToCFGConverter();
        CFG compCFG = cfgConverter.convert(functionDef);

        // UDG
        CFGToUDGConverter converter = new CFGToUDGConverter();
        converter.setAstAnalyzer(astAnalyzer);
        UseDefGraph useDefGraph = converter.convert(compCFG);

        // DefUseCFG
        CFGAndUDGToDefUseCFG defUseConverter = new CFGAndUDGToDefUseCFG();
        DefUseCFG defUseCFG = defUseConverter.convert(compCFG, useDefGraph);

        // Data Dependence Graph
        DDGCreator ddgCreator = new DDGCreator();
        DDG ddg = ddgCreator.createForDefUseCFG(defUseCFG);

        // Control Dependence Graph
        CDGCreator cdgCreator = new CDGCreator();
        CDG cdg = cdgCreator.create(compCFG);

        CPG cpg = new CPG();
        cpg.initCFGEdges(compCFG);
        cpg.initCDGEdges(cdg);
        cpg.initDDGEdges(ddg);

        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        return;
    }


    private CPP14Parser getParser(String code){
        ANTLRInputStream input = new ANTLRInputStream(code);
        CPP14Lexer lexer = new CPP14Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CPP14Parser parser = new CPP14Parser(tokens);
        return parser;
    }
}