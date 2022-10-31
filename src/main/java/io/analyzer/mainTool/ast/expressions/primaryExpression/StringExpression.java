package io.analyzer.mainTool.ast.expressions.primaryExpression;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class StringExpression extends PrimaryExpression {
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
