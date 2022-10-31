package io.analyzer.mainTool.ast.statements.jump;

import io.analyzer.mainTool.ast.statements.Statement;
import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class ContinueStatement extends Statement {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
