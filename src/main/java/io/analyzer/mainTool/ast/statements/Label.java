package io.analyzer.mainTool.ast.statements;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.expressions.Expression;
import io.analyzer.mainTool.ast.ASTNodeVisitor;

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
