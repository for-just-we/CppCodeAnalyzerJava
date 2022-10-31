package parsing.CPP.builder;

import io.analyzer.mainTool.antlr.Cpp.CPP14Lexer;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.parsing.CPP.builder.FunctionDefBuilder;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

public class FunctionDefBuilderTest {
    @Test
    public void test(){
        String code = "struct ST* c1::func(unsigned int *a = NULL, struct ST s){\n" +
                "    int a = 1;\n" +
                "}";

        String code1 = "char* func1(unsigned int a[], int aa = 1){\n" +
                "    int c = a[0] + aa;\n" +
                "}";


        String code2 = "#ifdef INCLUDEMAIN\n" +
                "\n" +
                "int main(int argc, char * argv[])\n" +
                "{\n" +
                "    /* seed randomness */\n" +
                "    srand( (unsigned int)time(NULL) );\n" +
                "#ifndef OMITGOOD\n" +
                "    printLine(\"Calling good()...\");\n" +
                "    CWE127_Buffer_Underread__wchar_t_declare_memmove_68_good();\n" +
                "    printLine(\"Finished good()\");\n" +
                "#endif /* OMITGOOD */\n" +
                "#ifndef OMITBAD\n" +
                "    printLine(\"Calling bad()...\");\n" +
                "    CWE127_Buffer_Underread__wchar_t_declare_memmove_68_bad();\n" +
                "    printLine(\"Finished bad()\");\n" +
                "#endif /* OMITBAD */\n" +
                "    return 0;\n" +
                "}\n" +
                "\n" +
                "#endif";

        // 解析函数名和变量列表
        FunctionDefBuilder defBuilder = new FunctionDefBuilder();
        CPP14Parser parser = getParser(code2);
        CPP14Parser.FunctiondefinitionContext tree = parser.functiondefinition();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(defBuilder, tree);
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