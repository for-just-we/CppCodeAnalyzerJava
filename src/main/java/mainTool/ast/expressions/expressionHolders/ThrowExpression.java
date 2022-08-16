package mainTool.ast.expressions.expressionHolders;

import mainTool.ast.ASTNode;
import mainTool.ast.expressions.Expression;
import mainTool.ast.ASTNodeVisitor;

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
