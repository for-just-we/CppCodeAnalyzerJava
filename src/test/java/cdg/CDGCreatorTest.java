package cdg;


import mainTool.antlr.Cpp.CPP14Lexer;
import mainTool.antlr.Cpp.CPP14Parser;
import mainTool.ast.functionDef.FunctionDef;
import mainTool.cdg.CDG;
import mainTool.cdg.CDGCreator;
import mainTool.cfg.ASTToCFGConverter;
import mainTool.cfg.CFG;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import mainTool.parsing.CPP.builder.FunctionDefBuilder;

public class CDGCreatorTest {

    String code = "static void goodG2B2()\n" +
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

    String code1 = "void CWE124_Buffer_Underwrite__CWE839_fscanf_17_bad()\n" +
            "{\n" +
            "    int i,j;\n" +
            "    int data;\n" +
            "    /* Initialize data */\n" +
            "    data = -1;\n" +
            "    for(i = 0; i < 1; i++)\n" +
            "    {\n" +
            "        /* POTENTIAL FLAW: Read data from the console using fscanf() */\n" +
            "        fscanf(stdin, \"%d\", &data);\n" +
            "    }\n" +
            "    for(j = 0; j < 1; j++)\n" +
            "    {\n" +
            "        {\n" +
            "            int i;\n" +
            "            int buffer[10] = { 0 };\n" +
            "            /* POTENTIAL FLAW: Attempt to access a negative index of the array\n" +
            "            * This code does not check to see if the array index is negative */\n" +
            "            if (data < 10)\n" +
            "            {\n" +
            "                buffer[data] = 1;\n" +
            "                /* Print the array values */\n" +
            "                for(i = 0; i < 10; i++)\n" +
            "                {\n" +
            "                    printIntLine(buffer[i]);\n" +
            "                }\n" +
            "            }\n" +
            "            else\n" +
            "            {\n" +
            "                printLine(\"ERROR: Array index is negative.\");\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";


    String code2 = "void CWE121_Stack_Based_Buffer_Overflow__CWE129_fgets_01_bad()\n" +
            "{\n" +
            "    int data;\n" +
            "    /* Initialize data */\n" +
            "    data = -1;\n" +
            "    {\n" +
            "        char inputBuffer[CHAR_ARRAY_SIZE] = \"\";\n" +
            "        /* POTENTIAL FLAW: Read data from the console using fgets() */\n" +
            "        if (fgets(inputBuffer, CHAR_ARRAY_SIZE, stdin) != NULL)\n" +
            "        {\n" +
            "            /* Convert to int */\n" +
            "            data = atoi(inputBuffer);\n" +
            "        }\n" +
            "        else\n" +
            "        {\n" +
            "            printLine(\"fgets() failed.\");\n" +
            "        }\n" +
            "    }\n" +
            "    {\n" +
            "        int i;\n" +
            "        int buffer[10] = { 0 };\n" +
            "        /* POTENTIAL FLAW: Attempt to write to an index of the array that is above the upper bound\n" +
            "        * This code does check to see if the array index is negative */\n" +
            "        if (data >= 0)\n" +
            "        {\n" +
            "            buffer[data] = 1;\n" +
            "            /* Print the array values */\n" +
            "            for(i = 0; i < 10; i++)\n" +
            "            {\n" +
            "                printIntLine(buffer[i]);\n" +
            "            }\n" +
            "        }\n" +
            "        else\n" +
            "        {\n" +
            "            printLine(\"ERROR: Array index is negative.\");\n" +
            "        }\n" +
            "    }\n" +
            "}";
    @Test
    public void testFunctionDef(){

        // 创建AntlrAST
        CPP14Parser parser = getParser(code2);
        ParseTree tree = parser.functiondefinition();
        ParseTreeWalker walker = new ParseTreeWalker();

        // 创建自定义AST
        FunctionDefBuilder functionDefBuilder = new FunctionDefBuilder();
        walker.walk(functionDefBuilder, tree);
        FunctionDef functionDef = functionDefBuilder.functionDef;

        // 创建CFG
        ASTToCFGConverter cfgConverter = new ASTToCFGConverter();
        CFG compCFG = cfgConverter.convert(functionDef);

        // 创建CDG
        CDGCreator cdgCreator = new CDGCreator();
        CDG cdg = cdgCreator.create(compCFG);
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