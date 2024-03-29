package io.analyzer.mainTool.ast.expressions.primaryExpression;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class IntegerExpression extends PrimaryExpression{
    public Integer getValue() {
        try {
            String code = this.getEscapedCodeStr();
            return Integer.parseInt(code);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void decrement() {
        Integer value = getValue();
        value--;
        this.setCodeStr(value.toString());
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
