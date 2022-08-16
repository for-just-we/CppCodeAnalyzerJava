package mainTool.ast.statements;

import mainTool.ast.ASTNode;
import mainTool.ast.expressions.Expression;
import mainTool.ast.ASTNodeVisitor;

public class Label extends Statement {
    // goto语句一般为NormalLabel， Case语句为Case，Default为default
    public enum Type { Normal, Case, Default }

    public Type type;

    private Expression cond = null;

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof Expression)
            cond = (Expression) node;
    }

    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
