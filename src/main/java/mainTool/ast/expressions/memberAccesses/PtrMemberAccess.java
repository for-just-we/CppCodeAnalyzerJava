package mainTool.ast.expressions.memberAccesses;

import mainTool.ast.expressions.postfixExpressions.PostfixExpression;
import mainTool.ast.ASTNodeVisitor;

// a->f1
public class PtrMemberAccess extends PostfixExpression {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
