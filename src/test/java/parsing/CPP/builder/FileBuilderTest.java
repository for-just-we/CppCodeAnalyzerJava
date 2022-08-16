package parsing.CPP.builder;

import mainTool.antlr.Cpp.CPP14Lexer;
import mainTool.antlr.Cpp.CPP14Parser;
import mainTool.parsing.CPP.builder.FileBuilder;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.*;

public class FileBuilderTest {
    @Test
    public void test() throws IOException {
        String fileName = "src/test/testfiles/sard_test_cases/CWE190_test.c";
        String fileName1 = "src/test/testfiles/ComplexStruct.c";
        InputStream inputStream = new FileInputStream(fileName);

        // Antlr AST构建
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        CPP14Lexer lexer = new CPP14Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CPP14Parser parser = new CPP14Parser(tokens);
        parser.removeErrorListeners();	// remove all error listeners
        ParseTree tree = parser.translationunit();
        FileBuilder fileBuilder = new FileBuilder();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(fileBuilder, tree);
        return;
    }
}