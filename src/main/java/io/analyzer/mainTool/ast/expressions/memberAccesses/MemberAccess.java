package io.analyzer.mainTool.ast.expressions.memberAccesses;

import io.analyzer.mainTool.ast.ASTNodeVisitor;
import io.analyzer.mainTool.ast.expressions.postfixExpressions.PostfixExpression;

// 成员变量访问，对应 a.f1
public class MemberAccess extends PostfixExpression {
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
