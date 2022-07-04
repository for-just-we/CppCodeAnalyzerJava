package ast.expressions.postfixExpressions;

import ast.expressions.Identifier;
import ast.expressions.postfixExpressions.CallExpressionBase;
import ast.walking.ASTNodeVisitor;

public class NewExpression extends CallExpressionBase {
    private Identifier targetClass;

    public Identifier getTargetClass() {
        return this.targetClass;
    }

    public void setTargetClass(Identifier identifier) {
        this.targetClass = identifier;
        super.addChild(targetClass);
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
