package io.analyzer.mainTool.ast;

import io.analyzer.mainTool.ast.statements.CompoundStatement;
import io.analyzer.mainTool.ast.statements.ExpressionStatement;
import io.analyzer.mainTool.ast.statements.IdentifierDeclStatement;
import io.analyzer.mainTool.ast.statements.Label;
import io.analyzer.mainTool.ast.statements.blockstarters.*;
import io.analyzer.mainTool.ast.statements.jump.BreakStatement;
import io.analyzer.mainTool.ast.statements.jump.ContinueStatement;
import io.analyzer.mainTool.ast.statements.jump.GotoStatement;
import io.analyzer.mainTool.ast.statements.jump.ReturnStatement;
import io.analyzer.mainTool.ast.declaration.ClassDefStatement;
import io.analyzer.mainTool.ast.expressions.Identifier;
import io.analyzer.mainTool.ast.expressions.binaryExpressions.AssignmentExpr;
import io.analyzer.mainTool.ast.expressions.expressionHolders.Argument;
import io.analyzer.mainTool.ast.expressions.expressionHolders.Condition;
import io.analyzer.mainTool.ast.expressions.memberAccesses.MemberAccess;
import io.analyzer.mainTool.ast.expressions.postfixExpressions.CallExpression;
import io.analyzer.mainTool.ast.expressions.primaryExpression.PrimaryExpression;
import io.analyzer.mainTool.ast.functionDef.FunctionDef;
import io.analyzer.mainTool.ast.functionDef.Parameter;
import io.analyzer.mainTool.ast.functionDef.ParameterList;

public class ASTNodeVisitor {
    public void defaultHandler(ASTNode item) {
        // by default, redirect to visit(ASTNode item)
        visit((ASTNode) item);
    }

    public void visitChildren(ASTNode item) {
        int nChildren = item.getChildCount();

        for (int i = 0; i < nChildren; i++)
        {
            ASTNode child = item.getChild(i);
            child.accept(this);
        }
    }


    public void visit(ParameterList item)
    {
        defaultHandler(item);
    }

    public void visit(Parameter item)
    {
        defaultHandler(item);
    }

    public void visit(FunctionDef item)
    {
        defaultHandler(item);
    }

    public void visit(ClassDefStatement item)
    {
        defaultHandler(item);
    }

    public void visit(IdentifierDeclStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(ExpressionStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(CallExpression expression)
    {
        defaultHandler(expression);
    }

    public void visit(Condition expression)
    {
        defaultHandler(expression);
    }

    public void visit(AssignmentExpr expression)
    {
        defaultHandler(expression);
    }

    public void visit(PrimaryExpression expression)
    {
        defaultHandler(expression);
    }

    public void visit(Identifier expression)
    {
        defaultHandler(expression);
    }

    public void visit(MemberAccess expression)
    {
        defaultHandler(expression);
    }

    public void visit(Argument expression)
    {
        defaultHandler(expression);
    }

    public void visit(ReturnStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(GotoStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(ContinueStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(BreakStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(CompoundStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(IfStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(ForStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(WhileStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(DoStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(ForRangeStatement statementItem) { defaultHandler(statementItem); }

    public void visit(TryStatement statementItem) { defaultHandler(statementItem); }

    public void visit(Label statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(SwitchStatement statementItem)
    {
        defaultHandler(statementItem);
    }

    public void visit(ASTNode item) {
        visitChildren(item);
    }
}
