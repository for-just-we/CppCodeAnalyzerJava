package mainTool.ast.statements.jump;

import mainTool.ast.ASTNodeVisitor;

public class ReturnStatement extends JumpStatement {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
