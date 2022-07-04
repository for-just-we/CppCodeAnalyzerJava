package ast.expressions.expressionHolders;

import ast.expressions.Expression;

public class ExpressionHolder extends Expression {
    public String getEscapedCodeStr() {
        if (codeStr != null)
            return codeStr;

        Expression expr = getExpression();
        if (expr == null)
            return "";

        codeStr = expr.getEscapedCodeStr();
        return codeStr;
    }

    public Expression getExpression() {
        if (children == null)
            return null;
        return (Expression) children.get(0);
    }
}
