package io.analyzer.mainTool.ast.statements.blockstarters;

import io.analyzer.mainTool.ast.declaration.ForRangeInit;
import io.analyzer.mainTool.ast.ASTNode;

public class ForRangeStatement extends BlockStarter{
    private ASTNode forRangeInit = null;

    public ASTNode getForRangeInit() {
        return forRangeInit;
    }

    public void setForRangeInit(ASTNode forRangeInit) {
        this.forRangeInit = forRangeInit;
    }

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof ForRangeInit)
            forRangeInit = node;
        super.addChild(node);
    }
}
