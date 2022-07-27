package ast.expressions;

import ast.ASTNodeVisitor;

public class UnaryOp extends Expression{
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
