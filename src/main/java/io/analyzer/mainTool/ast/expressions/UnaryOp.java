package io.analyzer.mainTool.ast.expressions;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class UnaryOp extends Expression{
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
