package ast.statements.jump;

import ast.ASTNodeVisitor;

public class BreakStatement extends JumpStatement {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
