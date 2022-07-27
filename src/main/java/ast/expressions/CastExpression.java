package ast.expressions;

import ast.ASTNode;
import ast.ASTNodeVisitor;

// 类型转换，最多2个子结点
public class CastExpression extends Expression {
    Expression castTarget = null;
    Expression castExpression = null;

    @Override
    public void addChild(ASTNode expression) {
        if (castTarget == null)
            setCastTarget((Expression) expression);
        else
            setCastExpression((Expression) expression);
        // super.addChild(expression);
    }

    @Override
    public int getChildCount()
    {
        int childCount = 0;
        if (castTarget != null)
            childCount++;
        if (castExpression != null)
            childCount++;
        return childCount;
    }

    @Override
    public ASTNode getChild(int i)
    {
        if (i == 0)
            return castTarget;
        return castExpression;
    }

    public Expression getCastTarget() {
        return this.castTarget;
    }

    public void setCastTarget(Expression castTarget) {
        this.castTarget = castTarget;
        super.addChild(castTarget);
    }

    public Expression getCastExpression() {
        return this.castExpression;
    }

    public void setCastExpression(Expression castExpression) {
        this.castExpression = castExpression;
        super.addChild(castExpression);
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
