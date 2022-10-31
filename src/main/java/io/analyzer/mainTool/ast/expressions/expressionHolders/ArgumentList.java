package io.analyzer.mainTool.ast.expressions.expressionHolders;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

// ArgumentList可以接任意个（包括0个） Argument，没有参数的话就是一个没有child的 ArgumentList
public class ArgumentList extends ExpressionHolder {
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
