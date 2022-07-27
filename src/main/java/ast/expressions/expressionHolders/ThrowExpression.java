package ast.expressions.expressionHolders;

import ast.ASTNode;
import ast.expressions.Expression;
import ast.ASTNodeVisitor;

// throw expr
public class ThrowExpression extends ExpressionHolder{
    private Expression throwExpression;

    public Expression getThrowExpression() {
        return this.throwExpression;
    }

    public void setThrowExpression(Expression expression) {
        this.throwExpression = expression;
    }

    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof Expression)
            setThrowExpression((Expression) node);
        super.addChild(node);
    }
}
