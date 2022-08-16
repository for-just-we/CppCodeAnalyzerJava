package parsing.CPP.builder;

import mainTool.antlr.Cpp.CPP14Lexer;
import mainTool.antlr.Cpp.CPP14Parser;
import mainTool.parsing.CPP.builder.FunctionNameParamBuilder;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

public class FunctionNameParamBuilderTest {

    @Test
    public void test(){
        String code = "struct ST* c1::func(unsigned int *a = NULL, struct ST s){\n" +
                "    int a = 1;\n" +
                "}";
        // 解析函数名和变量列表
        FunctionNameParamBuilder nameParamBuilder = new FunctionNameParamBuilder();
        nameParamBuilder.completeType = "struct ST";

        CPP14Parser parser = getParser(code);
        CPP14Parser.FunctiondefinitionContext tree = parser.functiondefinition();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(nameParamBuilder, tree.declarator());
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