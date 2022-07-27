package ast.statements.blockstarters;

import ast.ASTNodeVisitor;

public class DoStatement extends BlockStarter {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
