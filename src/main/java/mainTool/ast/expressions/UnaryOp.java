package mainTool.ast.expressions;

import mainTool.ast.ASTNodeVisitor;

public class UnaryOp extends Expression{
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
