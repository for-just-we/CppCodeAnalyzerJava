package mainTool.ast.statements.jump;

import mainTool.ast.statements.Statement;
import mainTool.ast.ASTNodeVisitor;

public class ContinueStatement extends Statement {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
