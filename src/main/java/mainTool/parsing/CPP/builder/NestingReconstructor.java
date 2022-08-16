package mainTool.parsing.CPP.builder;

import mainTool.ast.ASTNode;
import mainTool.ast.expressions.Expression;
import mainTool.ast.expressions.expressionHolders.ExpressionHolder;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Stack;

public class NestingReconstructor {
    Stack<ASTNode> stack;

    public NestingReconstructor(Stack<ASTNode> aStack)
    {
        stack = aStack;
    }

    protected void addItemToParent(ASTNode expression) {
        ASTNode topOfStack = stack.peek();
        topOfStack.addChild(expression);
    }

    // 合并子表达式
    protected void consolidateSubExpression(ParserRuleContext ctx) {
        Expression expression = (Expression) stack.pop();
        expression.initializeFromContext(ctx);
        if (!(expression instanceof ExpressionHolder))
            expression = pullUpOnlyChild(expression);
        addItemToParent(expression);
    }

    // 修剪AST，如果一个父节点只有一个子结点那么该父节点被赋值给子结点
    private Expression pullUpOnlyChild(Expression expression) {
        if (expression.getChildCount() == 1)
            expression = (Expression) expression.getChild(0);
        return expression;
    }
}
