package ast.expressions.memberAccesses;

import ast.expressions.postfixExpressions.PostfixExpression;
import ast.ASTNodeVisitor;

// 成员变量访问，对应 a.f1
public class MemberAccess extends PostfixExpression {
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
