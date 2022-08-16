package mainTool.ast.expressions.primaryExpression;

import mainTool.ast.ASTNodeVisitor;

public class StringExpression extends PrimaryExpression {
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
