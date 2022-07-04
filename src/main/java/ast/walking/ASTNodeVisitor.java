package ast.walking;

import ast.ASTNode;
import ast.declaration.ClassDefStatement;
import ast.expressions.Identifier;
import ast.expressions.binaryExpressions.AssignmentExpr;
import ast.expressions.expressionHolders.Argument;
import ast.expressions.expressionHolders.Condition;
import ast.expressions.memberAccesses.MemberAccess;
import ast.expressions.postfixExpressions.CallExpression;
import ast.expressions.primaryExpression.PrimaryExpression;
import ast.functionDef.FunctionDef;
import ast.functionDef.Parameter;
import ast.functionDef.ParameterList;
import ast.statements.CompoundStatement;
import ast.statements.ExpressionStatement;
import ast.statements.IdentifierDeclStatement;
import ast.statements.Label;
import ast.statements.blockstarters.*;
import ast.statements.jump.BreakStatement;
import ast.statements.jump.ContinueStatement;
import ast.statements.jump.GotoStatement;
import ast.statements.jump.ReturnStatement;

import java.util.Stack;

public class ASTNodeVisitor {
    protected Stack<Long> contextStack;

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

    public void visit(ReturnStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(GotoStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(ContinueStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(BreakStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(CompoundStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(IfStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(ForStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(WhileStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(DoStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(Label expression)
    {
        defaultHandler(expression);
    }

    public void visit(SwitchStatement expression)
    {
        defaultHandler(expression);
    }

    public void visit(ASTNode item) {
        visitChildren(item);
    }
}
