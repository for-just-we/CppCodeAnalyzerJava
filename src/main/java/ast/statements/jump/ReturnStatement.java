package ast.statements.jump;

import ast.ASTNodeVisitor;

public class ReturnStatement extends JumpStatement {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
