package parsing.CPP.builder;

import antlr.Cpp.CPP14Lexer;
import antlr.Cpp.CPP14Parser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.*;

public class FileBuilderTest {
    @Test
    public void test() throws IOException {
        String fileName = "src/test/testfiles/sard_test_cases/CWE_119_124_class_method_decl.c";
        String fileName1 = "src/test/testfiles/ComplexStruct.c";
        InputStream inputStream = new FileInputStream(fileName1);

        // Antlr AST构建
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        CPP14Lexer lexer = new CPP14Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CPP14Parser parser = new CPP14Parser(tokens);

        FileBuilder fileBuilder = new FileBuilder();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(fileBuilder, parser.translationunit());
        return;
    }
}