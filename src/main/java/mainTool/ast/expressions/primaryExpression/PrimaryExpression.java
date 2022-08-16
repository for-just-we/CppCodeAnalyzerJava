package mainTool.ast.expressions.primaryExpression;

import mainTool.ast.expressions.Expression;
import mainTool.ast.ASTNodeVisitor;

public class PrimaryExpression extends Expression {
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
