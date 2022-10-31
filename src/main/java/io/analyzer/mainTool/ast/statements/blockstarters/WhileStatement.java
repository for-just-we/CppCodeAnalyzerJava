package io.analyzer.mainTool.ast.statements.blockstarters;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class WhileStatement extends BlockStarter {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
