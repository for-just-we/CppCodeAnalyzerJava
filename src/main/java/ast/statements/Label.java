package ast.statements;

import ast.ASTNode;
import ast.expressions.Expression;
import ast.walking.ASTNodeVisitor;

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
