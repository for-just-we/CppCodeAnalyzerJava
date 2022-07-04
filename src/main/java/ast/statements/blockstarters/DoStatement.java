package ast.statements.blockstarters;

import ast.walking.ASTNodeVisitor;

public class DoStatement extends BlockStarter {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
