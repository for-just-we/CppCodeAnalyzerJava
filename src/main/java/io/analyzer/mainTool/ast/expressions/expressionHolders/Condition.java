package io.analyzer.mainTool.ast.expressions.expressionHolders;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class Condition extends ExpressionHolder {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
