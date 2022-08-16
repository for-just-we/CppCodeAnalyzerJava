package mainTool.ast.statements.blockstarters;

import mainTool.ast.ASTNodeVisitor;

public class DoStatement extends BlockStarter {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
