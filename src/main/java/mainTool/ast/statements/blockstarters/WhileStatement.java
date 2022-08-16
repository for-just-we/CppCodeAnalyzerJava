package mainTool.ast.statements.blockstarters;

import mainTool.ast.ASTNodeVisitor;

public class WhileStatement extends BlockStarter {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
