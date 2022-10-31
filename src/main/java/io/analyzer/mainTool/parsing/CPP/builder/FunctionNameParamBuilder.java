package io.analyzer.mainTool.parsing.CPP.builder;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.ASTNodeBuilder;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.ast.expressions.ClassStaticIdentifier;
import io.analyzer.mainTool.ast.expressions.Identifier;
import io.analyzer.mainTool.ast.expressions.primaryExpression.*;
import io.analyzer.mainTool.ast.functionDef.Parameter;
import io.analyzer.mainTool.ast.functionDef.ParameterList;
import io.analyzer.mainTool.ast.functionDef.ParameterType;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Stack;

public class FunctionNameParamBuilder extends ASTNodeBuilder {
    public Stack<ASTNode> stack;
    public String completeType;
    public Identifier funcName;

    public String curParamType = null; // 参数基础类型
    public String curParamCompleteType = null; // 参数基础类型
    // 参数完整类型

    public Stack<String> idType = new Stack();
    private final String varDecl = "VarDecl";

    public FunctionNameParamBuilder() {
        stack = new Stack<>();
        stack.push(new ParameterList());
    }

    @Override
    public void enterPtrDecl(CPP14Parser.PtrDeclContext ctx) {
        // 当前属于函数返回类型
        if (stack.peek() instanceof ParameterList)
            completeType += " *";
        else
            curParamCompleteType += " *";
    }

    @Override
    public void enterArrayDecl(CPP14Parser.ArrayDeclContext ctx) {
        if (stack.peek() instanceof Parameter)
            curParamCompleteType += " *";
    }

    // 参数列表
    @Override
    public void enterParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        if (stack.peek() instanceof ParameterList)
            stack.peek().initializeFromContext(ctx);
    }

    // 参数定义
    @Override
    public void enterParameterdeclaration(CPP14Parser.ParameterdeclarationContext ctx) {
        curParamType = "";
        Parameter parameter = new Parameter();
        ParameterType type = new ParameterType();
        parameter.setType(type);
        parameter.initializeFromContext(ctx);
        stack.push(parameter);
    }

    @Override
    public void exitParameterdeclaration(CPP14Parser.ParameterdeclarationContext ctx) {
        Parameter parameter = (Parameter) stack.pop();
        ParameterType type = parameter.getType();
        type.setBaseType(curParamType);
        type.setCompleteType(curParamCompleteType);

        ParameterList parameterList = (ParameterList) stack.peek();
        parameterList.addParameter(parameter);
        curParamType = null;
        curParamCompleteType = null;
    }

    @Override
    public void enterDeclarator(CPP14Parser.DeclaratorContext ctx) {
        if (stack.peek() instanceof Parameter){
            curParamType = curParamType.strip();
            curParamCompleteType = curParamType;
        }
    }

    @Override
    public void enterOtherDecl(CPP14Parser.OtherDeclContext ctx) {
        idType.push(varDecl);
    }

    @Override
    public void exitOtherDecl(CPP14Parser.OtherDeclContext ctx) {
        idType.pop();
    }

    // 函数名
    // 类静态变量 className::varName
    @Override
    public void enterClassIdentifier(CPP14Parser.ClassIdentifierContext ctx) {
        ClassStaticIdentifier staticVariable = new ClassStaticIdentifier();
        stack.push(staticVariable);
    }

    @Override
    public void exitQualifiedid(CPP14Parser.QualifiedidContext ctx) {
        if (stack.peek() instanceof ClassStaticIdentifier){
            ClassStaticIdentifier staticVariable = (ClassStaticIdentifier) stack.pop();
            staticVariable.initializeFromContext(ctx);

            // 变量名
            if (stack.peek() instanceof ParameterList)
                funcName = staticVariable;
            // stack.peek().addChild(staticVariable);
        }
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        ASTNode parent = stack.peek();

        // 当前终端结点属于变量定义语句一部分并且不属于C++模板定义
        if (!idType.empty() && idType.peek().equals(varDecl)){
            // 普通的变量定义初始化
            curParamType += node.getText() + " ";
            return;
        }

        switch (node.getSymbol().getType()){
            // 字面量
            case CPP14Parser.Identifier:
                Identifier expr = new Identifier();
                expr.setCodeStr(node.getText());
                // 函数名
                if (stack.peek() instanceof ParameterList)
                    funcName = expr;
                else
                    parent.addChild(expr);
                break;

           // 默认值
            case CPP14Parser.Integerliteral:
            case CPP14Parser.Userdefinedintegerliteral:
                IntegerExpression integerExpression = new IntegerExpression();
                integerExpression.setCodeStr(node.getText());
                parent.addChild(integerExpression);
                break;

            case CPP14Parser.Floatingliteral:
            case CPP14Parser.Userdefinedfloatingliteral:
                DoubleExpression doubleExpression = new DoubleExpression();
                doubleExpression.setCodeStr(node.getText());
                parent.addChild(doubleExpression);
                break;

            case CPP14Parser.Characterliteral:
            case CPP14Parser.Userdefinedcharacterliteral:
                CharExpression charExpression = new CharExpression();
                charExpression.setCodeStr(node.getText());
                parent.addChild(charExpression);
                break;

            case CPP14Parser.Stringliteral:
            case CPP14Parser.Userdefinedstringliteral:
                StringExpression stringExpression = new StringExpression();
                stringExpression.setCodeStr(node.getText());
                parent.addChild(stringExpression);
                break;

            case CPP14Parser.True:
            case CPP14Parser.False:
                BoolExpression boolExpression = new BoolExpression();
                boolExpression.setCodeStr(node.getText());
                parent.addChild(boolExpression);
                break;

            case CPP14Parser.Nullptr:
                PointerExpression pointerExpression = new PointerExpression();
                pointerExpression.setCodeStr(node.getText());
                parent.addChild(pointerExpression);
                break;
        }
    }
}
