package io.analyzer.mainTool.ast.expressions.primaryExpression;

import io.analyzer.mainTool.ast.ASTNodeVisitor;
import io.analyzer.mainTool.ast.expressions.Expression;

public class PrimaryExpression extends Expression {
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
