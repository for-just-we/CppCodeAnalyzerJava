package io.analyzer.mainTool.ast.expressions.memberAccesses;

import io.analyzer.mainTool.ast.ASTNodeVisitor;
import io.analyzer.mainTool.ast.expressions.postfixExpressions.PostfixExpression;

// a->f1
public class PtrMemberAccess extends PostfixExpression {
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
