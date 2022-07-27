package ast.statements.blockstarters;

import ast.ASTNodeVisitor;

public class SwitchStatement extends BlockStarter {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
