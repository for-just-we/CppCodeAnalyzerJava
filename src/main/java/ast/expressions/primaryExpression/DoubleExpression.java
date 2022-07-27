package ast.expressions.primaryExpression;

import ast.ASTNodeVisitor;

public class DoubleExpression extends PrimaryExpression{
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
