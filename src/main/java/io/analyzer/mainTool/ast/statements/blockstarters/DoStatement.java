package io.analyzer.mainTool.ast.statements.blockstarters;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class DoStatement extends BlockStarter {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
