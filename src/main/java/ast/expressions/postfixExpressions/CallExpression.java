package ast.expressions.postfixExpressions;

import ast.ASTNode;
import ast.expressions.expressionHolders.ArgumentList;
import ast.expressions.Identifier;
import ast.walking.ASTNodeVisitor;

public class CallExpression extends CallExpressionBase {
    @Override
    public void addChild(ASTNode node) {
        if (node instanceof Identifier) {
            setTargetFunc((Identifier) node);
        } else if (node instanceof ArgumentList) {
            setArgumentList((ArgumentList) node);
        } else {
            super.addChild(node);
        }
    }

    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
