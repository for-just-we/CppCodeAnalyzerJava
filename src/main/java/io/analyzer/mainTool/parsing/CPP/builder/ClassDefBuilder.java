package io.analyzer.mainTool.parsing.CPP.builder;

import io.analyzer.mainTool.ast.ASTNodeBuilder;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.ast.declaration.ClassDefStatement;
import io.analyzer.mainTool.ast.expressions.ClassStaticIdentifier;
import io.analyzer.mainTool.ast.expressions.Identifier;
import io.analyzer.mainTool.ast.functionDef.FunctionDef;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;


// 目前类解析只管一件事情，就是解析内部成员函数
public class ClassDefBuilder extends ASTNodeBuilder {
    private Boolean enterClassDecl = false; //是否进入类定义作用域
    private Boolean isClassName = false; // 是否可能为类名
    private Identifier curClassName = null;

    public ClassDefStatement classDefStatement = null;

    private ParseTreeWalker walker = new ParseTreeWalker();

    @Override
    public void enterClassDecl(CPP14Parser.ClassDeclContext ctx) {
        classDefStatement = new ClassDefStatement();
        enterClassDecl = true;
    }

    @Override
    public void exitClassDecl(CPP14Parser.ClassDeclContext ctx) {
        enterClassDecl = false;
    }

    @Override
    public void enterClassname(CPP14Parser.ClassnameContext ctx) {
        if (enterClassDecl)
            isClassName = true;
    }

    @Override
    public void exitClassname(CPP14Parser.ClassnameContext ctx) {
        if (enterClassDecl)
            isClassName = false;
    }

    // 成员函数定义
    @Override
    public void enterMemberFuncDecl(CPP14Parser.MemberFuncDeclContext ctx) {
        FunctionDefBuilder defBuilder = new FunctionDefBuilder();
        CPP14Parser.FunctiondefinitionContext tree = ctx.functiondefinition();

        walker.walk(defBuilder, tree);

        FunctionDef functionDef = defBuilder.functionDef;
        if (functionDef.name instanceof ClassStaticIdentifier)
            throw new RuntimeException("Inner method declaration should not contain class name");
        ClassStaticIdentifier staticIdentifier = new ClassStaticIdentifier();
        staticIdentifier.setClassName(curClassName);
        staticIdentifier.setVarName(functionDef.name);
        functionDef.replaceName(staticIdentifier);
        classDefStatement.functionDefs.add(functionDef);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        if (node.getSymbol().getType() == CPP14Parser.Identifier && isClassName){
            Identifier identifier = new Identifier();
            identifier.setCodeStr(node.getText());
            curClassName = identifier;
            classDefStatement.name = identifier;
        }
    }
}
