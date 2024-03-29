package io.analyzer.mainTool.ast.expressions;

import io.analyzer.mainTool.ast.ASTNode;

public class Expression extends ASTNode {
    private String operator = "";

    public void replaceFirstChild(ASTNode node) {
        children.removeFirst();
        children.addFirst(node);
    }

    public void setOperator(String text)
    {
        operator = text;
    }

    public String getOperator()
    {
        return operator;
    }
}
