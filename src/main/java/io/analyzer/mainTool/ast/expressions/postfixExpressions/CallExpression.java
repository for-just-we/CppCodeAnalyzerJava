package io.analyzer.mainTool.ast.expressions.postfixExpressions;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.ASTNodeVisitor;
import io.analyzer.mainTool.ast.expressions.expressionHolders.ArgumentList;
import io.analyzer.mainTool.ast.expressions.Identifier;

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
