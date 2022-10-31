package io.analyzer.mainTool.ast.expressions.primaryExpression;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class BoolExpression extends PrimaryExpression{
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
