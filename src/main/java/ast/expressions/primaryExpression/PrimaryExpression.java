package ast.expressions.primaryExpression;

import ast.expressions.Expression;
import ast.ASTNodeVisitor;

public class PrimaryExpression extends Expression {
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
