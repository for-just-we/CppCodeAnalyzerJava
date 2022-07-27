package ast.statements.blockstarters;

import ast.ASTNodeVisitor;

public class WhileStatement extends BlockStarter {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
