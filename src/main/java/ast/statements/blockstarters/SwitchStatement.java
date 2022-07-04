package ast.statements.blockstarters;

import ast.statements.blockstarters.BlockStarter;
import ast.walking.ASTNodeVisitor;

public class SwitchStatement extends BlockStarter {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
