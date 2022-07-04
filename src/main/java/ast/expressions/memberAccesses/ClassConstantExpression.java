package ast.expressions.memberAccesses;

import ast.expressions.Expression;
import ast.expressions.primaryExpression.StringExpression;
import ast.walking.ASTNodeVisitor;

public class ClassConstantExpression extends MemberAccess {
    private Expression classExpression = null;
    private StringExpression constantName = null;

    public Expression getClassExpression() {
        return this.classExpression;
    }

    public void setClassExpression(Expression classExpression) {
        this.classExpression = classExpression;
        super.addChild(classExpression);
    }

    public StringExpression getConstantName() {
        return this.constantName;
    }

    public void setConstantName(StringExpression constantName) {
        this.constantName = constantName;
        super.addChild(constantName);
    }
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
