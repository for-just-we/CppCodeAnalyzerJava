package io.analyzer.mainTool.ast.statements.jump;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class BreakStatement extends JumpStatement {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
