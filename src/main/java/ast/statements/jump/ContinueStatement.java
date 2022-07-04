package ast.statements.jump;

import ast.statements.Statement;
import ast.walking.ASTNodeVisitor;

public class ContinueStatement extends Statement {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
