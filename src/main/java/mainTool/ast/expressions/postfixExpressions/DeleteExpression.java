package mainTool.ast.expressions.postfixExpressions;

import mainTool.ast.ASTNode;
import mainTool.ast.expressions.Expression;
import mainTool.ast.ASTNodeVisitor;

public class DeleteExpression extends CallExpressionBase {
    // delete掉的可能变量名，数组引用或者指针引用
    private Expression target;

    public Expression getTarget() {
        return this.target;
    }

    public void setTarget(Expression target){
        this.target = target;
    }

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof Expression && target == null)
            setTarget((Expression) node);
        super.addChild(node);
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
