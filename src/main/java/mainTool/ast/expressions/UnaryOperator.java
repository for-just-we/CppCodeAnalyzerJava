package mainTool.ast.expressions;

import mainTool.ast.ASTNodeVisitor;

public class UnaryOperator extends Expression{
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}