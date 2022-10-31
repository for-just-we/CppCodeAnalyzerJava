package parsing.CPP.builder;


import io.analyzer.mainTool.antlr.Cpp.CPP14Lexer;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.parsing.CPP.builder.ClassDefBuilder;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

public class ClassDefBuilderTest {
    @Test
    public void test(){
        String code = "class c1{\n" +
                "    private:\n" +
                "    unsigned int* cccc(int a[], char **p){\n" +
                "    int aa = char(c);\n" +
                "    }\n" +
                "};";
        // 解析函数名和变量列表
        ClassDefBuilder defBuilder = new ClassDefBuilder();
        CPP14Parser parser = getParser(code);
        CPP14Parser.TranslationunitContext tree = parser.translationunit();
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