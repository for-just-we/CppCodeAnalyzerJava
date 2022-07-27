package cfg.CPP;

import antlr.Cpp.CPP14Lexer;
import antlr.Cpp.CPP14Parser;
import ast.ASTNode;
import ast.functionDef.FunctionDef;
import ast.statements.CompoundStatement;
import ast.statements.Statement;
import ast.statements.blockstarters.*;
import cfg.ASTToCFGConverter;
import cfg.CFG;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import parsing.CPP.builder.FunctionContentBuilder;
import parsing.CPP.builder.FunctionDefBuilder;

public class CCFGFactoryTest {
    @Test
    public void testCompoundStatement(){
        String stmt = "{ exit(-1); *p = a * b; int s = *p + d; }";
        CPP14Parser parser = getParser(stmt);
        ParseTree tree = parser.statement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder contentBuilder = new FunctionContentBuilder();
        contentBuilder.stack.push(new CompoundStatement());
        walker.walk(contentBuilder, tree);
        CompoundStatement compoundStatement = (CompoundStatement) contentBuilder.stack.pop();
        CFG compCFG =  CCFGFactory.newInstance(compoundStatement);
        return;
    }

    @Test
    public void testSelectionStatement(){
        String ifstmt = "if (a == 1)\n" +
                "    a = 1;\n" +
                "else if (a == 2)\n" +
                "    a = 2;\n" +
                "else \n" +
                "    a = 3;";

        String ifCode1 = "if (staticFalse){ exit(-1); goto loop; *p = a * b; return a + 1; }";

        String switchCode = "switch (staticTrue){\n" +
                "    case 1:\n" +
                "        test::a = 1;\n" +
                "        break;\n" +
                "    case 2:\n" +
                "        a = 2;\n" +
                "        break;\n" +
                "    default:\n" +
                "        a = 3;\n" +
                "}";

        CPP14Parser parser = getParser(switchCode);
        ParseTree tree = parser.selectionstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new Statement());
        walker.walk(builder, tree);
        // IfStatement ifStatement = (IfStatement) builder.stack.pop();
        SwitchStatement switchStatement = (SwitchStatement) builder.stack.pop();
        CFG compCFG =  CCFGFactory.newInstance(switchStatement);
        return;
    }

    @Test
    public void testWhile(){
        String stmt = "while(x <= 1){\n" +
                "    (*x)++;\n" +
                "    ++*x;\n" +
                "    if (cond)\n" +
                "        break;\n" +
                "    func(a);\n" +
                "}";
        CPP14Parser parser = getParser(stmt);
        ParseTree tree = parser.iterationstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new Statement());
        walker.walk(builder, tree);
        WhileStatement whileStatement = (WhileStatement) builder.stack.pop();
        CFG compCFG =  CCFGFactory.newInstance(whileStatement);
        return;
    }

    @Test
    public void testDoWhile(){
        String stmt = "do {\n" +
                "    int a = sizeof(int);\n" +
                "    b = sizeof(a);\n" +
                "}while(a + b <= 4);";

        CPP14Parser parser = getParser(stmt);
        ParseTree tree = parser.iterationstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new Statement());
        walker.walk(builder, tree);
        DoStatement doStatement = (DoStatement) builder.stack.pop();
        CFG compCFG =  CCFGFactory.newInstance(doStatement);
        return;
    }

    @Test
    public void testFor(){
        String stmt = "for(int i = 0; i < 10; ++i){ \n" +
                "    int a = 1;\n" +
                "    b = c + a;" +
                "    if (a > 0)\n" +
                "        break;\n" +
                "    func(a);\n" +
                "}";

        CPP14Parser parser = getParser(stmt);
        ParseTree tree = parser.iterationstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new Statement());
        walker.walk(builder, tree);
        ForStatement forStatement = (ForStatement) builder.stack.pop();
        CFG compCFG =  CCFGFactory.newInstance(forStatement);
        return;
    }

    @Test
    public void testForRange(){
        String stmt3 = "for (unsigned int * p: vec){\n" +
                "   unsigned int a = b + c, d{a}, e(8);\n" +
                "   char source[100], *dst[100], p[50][40];\n" +
                "   if (staticTrue)\n" +
                "       break;\n" +
                "   func(a);\n" +
                "   }";

        CPP14Parser parser = getParser(stmt3);
        ParseTree tree = parser.iterationstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new Statement());
        walker.walk(builder, tree);
        ForRangeStatement forRangeStatement = (ForRangeStatement) builder.stack.pop();
        CFG compCFG =  CCFGFactory.newInstance(forRangeStatement);
        return;
    }

    @Test
    public void testTry(){
        String code = "try{\n" +
                "        const int& a = 1;\n" +
                "    }catch(const int& e){\n" +
                "    }catch(...){\n" +
                "    }";

        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.tryblock();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new Statement());
        walker.walk(builder, tree);
        TryStatement tryStatement = (TryStatement) builder.stack.pop();
        CFG compCFG =  CCFGFactory.newInstance(tryStatement);
        return;
    }

    @Test
    public void testFunctionDef(){
        String code = "static void goodG2B2(int a = 1)\n" +
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

        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.functiondefinition();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionDefBuilder functionDefBuilder = new FunctionDefBuilder();
        walker.walk(functionDefBuilder, tree);
        FunctionDef functionDef = functionDefBuilder.functionDef;
        ASTToCFGConverter cfgConverter = new ASTToCFGConverter();
        CFG compCFG = cfgConverter.convert(functionDef);
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