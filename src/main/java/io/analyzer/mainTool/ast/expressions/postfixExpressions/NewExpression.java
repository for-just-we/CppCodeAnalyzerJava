package io.analyzer.mainTool.ast.expressions.postfixExpressions;

import io.analyzer.mainTool.ast.ASTNodeVisitor;
import io.analyzer.mainTool.ast.expressions.Identifier;

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
