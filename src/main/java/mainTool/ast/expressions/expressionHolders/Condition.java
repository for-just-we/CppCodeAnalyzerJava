package mainTool.ast.expressions.expressionHolders;

import mainTool.ast.ASTNodeVisitor;

public class Condition extends ExpressionHolder {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
