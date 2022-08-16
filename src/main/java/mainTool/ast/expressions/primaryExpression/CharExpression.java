package mainTool.ast.expressions.primaryExpression;

import mainTool.ast.ASTNodeVisitor;

public class CharExpression extends PrimaryExpression{
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
