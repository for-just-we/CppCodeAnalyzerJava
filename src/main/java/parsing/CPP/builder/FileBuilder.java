package parsing.CPP.builder;

import antlr.Cpp.CPP14Parser;
import ast.ASTNodeBuilder;
import ast.declaration.ClassDefStatement;
import ast.functionDef.FunctionDef;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;


// 解析一个c文件中包含的所有类/结构体定义和函数定义
public class FileBuilder extends ASTNodeBuilder {
    public List<ClassDefStatement> classDefs = new ArrayList<>();
    public List<FunctionDef> functionDefs = new ArrayList<>();
    private ParseTreeWalker walker = new ParseTreeWalker();

    @Override
    public void enterClassDecl(CPP14Parser.ClassDeclContext ctx) {
        ClassDefBuilder defBuilder = new ClassDefBuilder();
        walker.walk(defBuilder, ctx);
        classDefs.add(defBuilder.classDefStatement);
    }

    @Override
    public void enterFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        if (ctx.parent instanceof CPP14Parser.MemberFuncDeclContext)
            return;
        FunctionDefBuilder defBuilder = new FunctionDefBuilder();
        walker.walk(defBuilder, ctx);
        functionDefs.add(defBuilder.functionDef);
    }
}
