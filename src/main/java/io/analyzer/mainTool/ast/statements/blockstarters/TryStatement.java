package io.analyzer.mainTool.ast.statements.blockstarters;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.statements.CompoundStatement;

public class TryStatement extends BlockStarter{
    private CompoundStatement content = null;
    private CatchList catchList = new CatchList();

    public CompoundStatement getContent() {
        return this.content;
    }

    public void setContent(CompoundStatement content) {
        this.content = content;
        super.addChild(content);
    }

    public CatchList getCatchList() {
        return this.catchList;
    }

    public void setCatchList(CatchList catchList) {
        this.catchList = catchList;
        super.addChild(catchList);
    }

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof CompoundStatement && getChildCount() == 0)
            setContent((CompoundStatement) node);
        else if (node instanceof CatchList)
            setCatchList((CatchList) node);
        else
            super.addChild(node);
    }
}
