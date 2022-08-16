package mainTool.ast.statements.blockstarters;

import mainTool.ast.ASTNode;
import mainTool.ast.declaration.ForRangeInit;

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
