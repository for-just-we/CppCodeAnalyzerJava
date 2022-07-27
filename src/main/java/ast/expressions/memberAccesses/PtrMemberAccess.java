package ast.expressions.memberAccesses;

import ast.expressions.postfixExpressions.PostfixExpression;
import ast.ASTNodeVisitor;

// a->f1
public class PtrMemberAccess extends PostfixExpression {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
