package io.analyzer.mainTool.ast.expressions.expressionHolders;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.ASTNodeVisitor;
import io.analyzer.mainTool.ast.expressions.Expression;

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
