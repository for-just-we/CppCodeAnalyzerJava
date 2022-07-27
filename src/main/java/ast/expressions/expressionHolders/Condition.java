package ast.expressions.expressionHolders;

import ast.ASTNodeVisitor;

public class Condition extends ExpressionHolder {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
