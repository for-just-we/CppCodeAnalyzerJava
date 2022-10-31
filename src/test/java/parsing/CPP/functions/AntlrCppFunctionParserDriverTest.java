package parsing.CPP.functions;


import io.analyzer.mainTool.antlr.Cpp.CPP14Lexer;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.ast.expressions.binaryExpressions.AssignmentExpr;
import io.analyzer.mainTool.ast.statements.CompoundStatement;
import io.analyzer.mainTool.ast.statements.Statement;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import io.analyzer.mainTool.parsing.CPP.builder.FunctionContentBuilder;

public class AntlrCppFunctionParserDriverTest {
    @Test
    public void test(){
        String code1 = "static void renameTestSchema(\n" +
                "  Parse *pParse,                  /* Parse context */\n" +
                "  const char *zDb,                /* Name of db to verify schema of */\n" +
                "  int bTemp,                      /* True if this is the temp db */\n" +
                "  const char *zWhen,              /* \"when\" part of error message */\n" +
                "  int bNoDQS                      /* Do not allow DQS in the schema */\n" +
                "){\n" +
                "  pParse->colNamesSet = 1;\n" +
                "  sqlite3NestedParse(pParse, \n" +
                "      \"SELECT 1 \"\n" +
                "      \"FROM \\\"%w\\\".\" LEGACY_SCHEMA_TABLE \" \"\n" +
                "      \"WHERE name NOT LIKE 'sqliteX_%%' ESCAPE 'X'\"\n" +
                "      \" AND sql NOT LIKE 'create virtual%%'\"\n" +
                "      \" AND sqlite_rename_test(%Q, sql, type, name, %d, %Q, %d)=NULL \",\n" +
                "      zDb,\n" +
                "      zDb, bTemp, zWhen, bNoDQS\n" +
                "  );\n" +
                "\n" +
                "  if( bTemp==0 ){\n" +
                "    sqlite3NestedParse(pParse, \n" +
                "        \"SELECT 1 \"\n" +
                "        \"FROM temp.\" LEGACY_SCHEMA_TABLE \" \"\n" +
                "        \"WHERE name NOT LIKE 'sqliteX_%%' ESCAPE 'X'\"\n" +
                "        \" AND sql NOT LIKE 'create virtual%%'\"\n" +
                "        \" AND sqlite_rename_test(%Q, sql, type, name, 1, %Q, %d)=NULL \",\n" +
                "        zDb, zWhen, bNoDQS\n" +
                "    );\n" +
                "  }\n" +
                "}";
        String code = "void main(){\n" +
                "\tint a = sizeof(int);\n" +
                "}";
        CPP14Parser parser = getParser(code1);
        ParseTree tree = parser.translationunit();
        return;
    }

    @Test
    public void testFunctionCall(){
        String code = "func(a, b + c);";
        String code1 = "func();";
        CPP14Parser parser = getParser(code1);
        ParseTree tree = parser.statement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testSizeofExpr(){
        String code = "sizeof a";
        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.sizeofExpression();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new AssignmentExpr());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testSimpleCast(){
        String code = "char(a + b * c)";
        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.postfixexpression();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new AssignmentExpr());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testCast(){
        String code = "(char)a";
        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.castexpression();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new AssignmentExpr());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testCppCast(){
        String code = "static_cast<unsigned int>(7.987);";
        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.postfixexpression();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new AssignmentExpr());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testIdentifierDeclSimple(){
        String code = "unsigned int a, *p, **pp;";
        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.simpledeclaration();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testIdentifierDeclWithAssign(){
        // typedef
        String codeType = "typedef long long LL;";

        // C语言初始化
        String code = "unsigned int a = b + c, d{a}, e(8);";
        String code1 = "char aa ='A', bb{'B'}, cc('C'), **dd(NULL);";
        String code2 = "char source[100], *dst[100], p[50][40];"; //数组定义
        String code3 = "char source[100] = L'A';"; //数组初始化
        String code4 = "int data[100] = {1, 2, 3, 4};";
        String code5 = "struct ST s = {1, 'a'};"; //结构体初始化
        String code6 = "struct ST s(1, 'a');"; //构造函数初始化
        String code7 = "int aa = b > 1 ? 1 : a;"; // 三目表达式初始化赋值

        // C++初始化
        String cppCpde = "vector<int> v4{10,1};";

        // const int
        String Ccode = "const int a = char(1);";
        String autoCode = "auto a = &i;";

        // static variable
        String staticCode = "unsigned int test::m_value1 = 0, ::a = 1, *bb(4);";
        String staticCode1 = "singleton *singleton::m_instance= NULL;";

        CPP14Parser parser = getParser(code);
        ParseTree tree = parser.simpledeclaration();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testNewExpression(){
        String code = "unsigned int **p = new unsigned int* [10];";
        String code1 = "int *a = new(buf) int[a+b];";
        String code2 = "Fun* ptr1 = new Fun;";

        CPP14Parser parser = getParser(code2);
        ParseTree tree = parser.simpledeclaration();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testDeleteExpression(){
        String baseDelete = "delete [] p;";
        String arrayDelete = "delete [] p[i][j];";
        String ptrDelete = "delete [] *(p+1);";

        CPP14Parser parser = getParser(arrayDelete);
        ParseTree tree = parser.deleteexpression();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testThrowExpression(){
        String throwExpr = "throw -1;";
        String throwExpr2 = "throw \"error msg\";";
        CPP14Parser parser = getParser(throwExpr2);
        ParseTree tree = parser.expressionstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testStd(){
        String code = "std::cout << \"yes\" << a << endl;";
        String code1 = "recvResult = recv(connectSocket, (char*)(data+dataLen) , sizeof(char)*(FILENAME_MAX-dataLen-1), 0);";
        CPP14Parser parser = getParser(code1);
        ParseTree tree = parser.expressionstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new CompoundStatement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testSelectionStatement(){
        String ifElseCode = "if (a == 1)\n" +
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
        //IfStatement ifStatement =
        return;
    }

    @Test
    public void testMemberAccess(){
        String memberaccessExpr = "a.x = 1;";
        String memberCall = "a.f(x, y);";

        CPP14Parser parser = getParser(memberCall);
        ParseTree tree = parser.expressionstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new Statement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testArrayAccess(){
        String stmt = "arr[i] = 1;";
        String stmt1 = "*(p + i) = 2;";
        String stmt2 = "f.ar[i] = 1;";

        CPP14Parser parser = getParser(stmt1);
        ParseTree tree = parser.expressionstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new Statement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testIteration(){
        String stmt = "while(x <= 1){\n" +
                "    x++;\n" +
                "    ++x;\n" +
                "}";

        String stmt1 = "do {\n" +
                "    int a = sizeof(int);\n" +
                "    b = sizeof(a);\n" +
                "}while(a + b <= 4);";

        String stmt2 = "for(int i = 0; i < 10; ++i);";

        String stmt3 = "for (unsigned int * p: vec){\n" +
                "   unsigned int a = b + c, d{a}, e(8);\n" +
                "   char source[100], *dst[100], p[50][40];\n" +
                "   }";

        CPP14Parser parser = getParser(stmt3);
        ParseTree tree = parser.iterationstatement();
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionContentBuilder builder = new FunctionContentBuilder();
        builder.stack.push(new Statement());
        walker.walk(builder, tree);
        return;
    }

    @Test
    public void testTryCatch(){
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