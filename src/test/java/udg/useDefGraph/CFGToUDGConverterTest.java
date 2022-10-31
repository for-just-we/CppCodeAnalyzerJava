package udg.useDefGraph;


import io.analyzer.mainTool.antlr.Cpp.CPP14Lexer;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.ast.statements.CompoundStatement;
import io.analyzer.mainTool.ast.statements.ExpressionStatement;
import io.analyzer.mainTool.ast.statements.IdentifierDeclStatement;
import io.analyzer.mainTool.udg.useDefGraph.UseOrDef;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import io.analyzer.mainTool.parsing.CPP.builder.FunctionContentBuilder;
import io.analyzer.mainTool.udg.ASTNodeASTProvider;
import io.analyzer.mainTool.udg.useDefAnalysis.ASTDefUseAnalyzer;
import io.analyzer.mainTool.udg.useDefAnalysis.CalleeInfos;

import java.util.Collection;

public class CFGToUDGConverterTest {

    ASTDefUseAnalyzer astAnalyzer = new ASTDefUseAnalyzer();
    CalleeInfos calleeInfos = new CalleeInfos();


    @Test
    public void testFuncCallStmt(){
        calleeInfos.addArgDef("memcpy", 0);
        calleeInfos.addArgUse("memcpy", 1);
        calleeInfos.addArgDef("memmove", 0);
        calleeInfos.addArgUse("memmove", 1);
        calleeInfos.addArgDef("memset", 0);
        calleeInfos.addArgDef("fgets", 0);
        calleeInfos.addArgDef("recv", 1);

        calleeInfos.addArgDefStartIds("scanf", 1);
        astAnalyzer.setCalleeInfos(calleeInfos);

        // function call
        String code = "memcpy(data, source, 100*sizeof(char));"; // 定义了 *data，使用了 data, source, *source
        String code1 = "memset(source, 'C' ,100- 1);"; // 定义了 *source，使用了source
        String code2 = "scanf(\"%d-%d\", &a, &b);"; // 定义了 a, b, 使用了 &a, &b
        String code3 = "fgets(inputBuffer, CHAR_ARRAY_SIZE, stdin);";
        String code4 = "fgets(data+dataLen, (int)(FILENAME_MAX - dataLen), stdin);";
        String code5 = "recvResult = recv(connectSocket, (char*)(data+dataLen) , " +
                "sizeof(char)*(FILENAME_MAX-dataLen-1), 0);";
        String code6 = "recv(connectSocket, (char*)(data+dataLen) , sizeof(char)*(FILENAME_MAX-dataLen-1), 0);";

        // ptr access
        String ptrAccessCode = "*(p + 1 + i) = *(a + j);";

        // array access
        String arrayAccessCode = "p[1 + i] = a[j][i];";
        String arrayAccessCode1 = "a[i][j] = b[1 + i];";

        // struct access
        String structAccCode = "foo.bar = 10;";
        String structAccCode1 = "foo->bar = foo1.f1.f2;";
        String structAccCode2 = "structCharVoid->charFirst[(sizeof(structCharVoid->charFirst) / sizeof(char))-1] = '\0';";
        String structAccCode3 = "structCharVoid->voidSecond = (void*)SRC_STR;";
        String structAccCode4 = "memmove(structCharVoid->charFirst, SRC_STR, sizeof(*structCharVoid));";

        CPP14Parser parser = getParser(structAccCode4);
        ParseTree tree = parser.statement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        ExpressionStatement expressionStatement = (ExpressionStatement) builder.stack.peek().getChild(0);

        ASTNodeASTProvider provider = new ASTNodeASTProvider();
        provider.setNode(expressionStatement);
        Collection<UseOrDef> usesAndDefs = astAnalyzer.analyzeAST(provider);
        return;
    }

    @Test
    public void testIdentifierDecl(){
        String code = "char source[100] = '\0'";
        String code1 = "char* dst = (char*)malloc(sizeof(char)*100);";
        String code2 = "struct my_struct foo;";

        calleeInfos.addArgDef("recv", 1);
        astAnalyzer.setCalleeInfos(calleeInfos);

        CPP14Parser parser = getParser(code2);
        ParseTree tree = parser.statement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        IdentifierDeclStatement declStatement = (IdentifierDeclStatement) builder.stack.peek().getChild(0);

        ASTNodeASTProvider provider = new ASTNodeASTProvider();
        provider.setNode(declStatement);
        Collection<UseOrDef> usesAndDefs = astAnalyzer.analyzeAST(provider);
        return;
    }

    @Test
    public void testAssignment(){
        String code = "*p = a;";
        String code1 = "*p += a;";
        // String code2 = "int a = 1;";

        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.statement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        ExpressionStatement expressionStatement = (ExpressionStatement) builder.stack.peek().getChild(0);

        ASTNodeASTProvider provider = new ASTNodeASTProvider();
        provider.setNode(expressionStatement);
        Collection<UseOrDef> usesAndDefs = astAnalyzer.analyzeAST(provider);
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