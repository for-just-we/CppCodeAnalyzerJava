package mainTool.ast.expressions.postfixExpressions;

// 对应 x++ 或者 x--， ++x, --x
public class IncDecOp extends PostfixExpression{
    // isPost为true表示 ++/-- 在变量后面
    public boolean isPost = true;
}
