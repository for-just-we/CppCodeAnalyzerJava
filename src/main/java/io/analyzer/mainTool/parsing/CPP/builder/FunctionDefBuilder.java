package io.analyzer.mainTool.parsing.CPP.builder;

import io.analyzer.mainTool.ast.ASTNodeBuilder;
import io.analyzer.mainTool.ast.statements.CompoundStatement;
import io.analyzer.mainTool.ast.statements.Statement;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.ast.functionDef.FunctionDef;
import io.analyzer.mainTool.ast.functionDef.ParameterList;
import io.analyzer.mainTool.ast.functionDef.ParameterType;
import io.analyzer.mainTool.ast.functionDef.ReturnType;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class  FunctionDefBuilder extends ASTNodeBuilder {
    public FunctionDef functionDef;
    private ParseTreeWalker walker = new ParseTreeWalker();

    @Override
    public void enterFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        this.functionDef = new FunctionDef();

        // 解析返回值类型
        FunctionTypeBuilder typeBuilder = new FunctionTypeBuilder();
        walker.walk(typeBuilder, ctx.declspecifierseq());
        ReturnType returnType = new ReturnType();
        returnType.setBaseType(typeBuilder.getType().strip());
        // 设定函数返回类型
        this.functionDef.setReturnType(returnType);

        // 解析函数名和变量列表
        FunctionNameParamBuilder nameParamBuilder = new FunctionNameParamBuilder();
        nameParamBuilder.completeType = typeBuilder.getType().strip();
        walker.walk(nameParamBuilder, ctx.declarator());

        ParameterType parameterType = new ParameterType();
        parameterType.setBaseType(nameParamBuilder.curParamType);
        parameterType.setCompleteType(nameParamBuilder.curParamCompleteType);
        // 函数名
        this.functionDef.setName(nameParamBuilder.funcName);
        // 参数列表
        this.functionDef.setParameterList((ParameterList) nameParamBuilder.stack.peek());
        // 设置完整返回类型
        this.functionDef.returnType.setCompleteType(nameParamBuilder.completeType);

        // 解析函数名
        FunctionContentBuilder contentBuilder = new FunctionContentBuilder();
        contentBuilder.stack.push(new Statement());
        walker.walk(contentBuilder, ctx.functionbody());
        CompoundStatement content = (CompoundStatement) contentBuilder.stack.peek();
        content.initializeFromContext(ctx.functionbody());
        this.functionDef.setContent(content);
    }
}
