package mainTool.ast.expressions;

import mainTool.ast.ASTNodeVisitor;

public class Constant extends Expression{
    private Identifier identifier = null;

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
        super.addChild(identifier);
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
