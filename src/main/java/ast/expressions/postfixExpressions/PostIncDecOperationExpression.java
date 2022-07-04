package ast.expressions.postfixExpressions;

import ast.ASTNode;
import ast.expressions.Expression;
import ast.walking.ASTNodeVisitor;

public class PostIncDecOperationExpression extends PostfixExpression {
    private Expression variableExpression = null;

    public Expression getVariableExpression() {
        return this.variableExpression;
    }

    public void setVariableExpression(Expression variableExpression) {
        this.variableExpression = variableExpression;
        super.addChild(variableExpression);
    }

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof Expression) {
            setVariableExpression((Expression) node);
        } else {
            super.addChild(node);
        }
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
