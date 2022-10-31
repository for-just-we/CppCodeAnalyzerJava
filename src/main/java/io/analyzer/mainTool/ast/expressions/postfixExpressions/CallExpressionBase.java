package io.analyzer.mainTool.ast.expressions.postfixExpressions;

import io.analyzer.mainTool.ast.ASTNodeVisitor;
import io.analyzer.mainTool.ast.expressions.expressionHolders.ArgumentList;
import io.analyzer.mainTool.ast.expressions.Expression;

public class CallExpressionBase extends PostfixExpression {
    private Expression targetFunc = null; // Callee
    private ArgumentList argumentList = null;

    public Expression getTargetFunc() {
        return this.targetFunc;
    }

    public void setTargetFunc(Expression targetFunc) {
        this.targetFunc = targetFunc;
        super.addChild(targetFunc);
    }

    public ArgumentList getArgumentList() {
        return this.argumentList;
    }

    public void setArgumentList(ArgumentList argumentList) {
        this.argumentList = argumentList;
        super.addChild(argumentList);
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
