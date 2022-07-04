package ast.statements.jump;

import ast.statements.jump.JumpStatement;
import ast.walking.ASTNodeVisitor;

public class ReturnStatement extends JumpStatement {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
