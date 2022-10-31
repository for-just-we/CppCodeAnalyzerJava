package io.analyzer.mainTool.parsing.CPP.builder;

import io.analyzer.mainTool.ast.ASTNodeBuilder;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.ast.declaration.ClassDefStatement;
import io.analyzer.mainTool.ast.functionDef.FunctionDef;
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
