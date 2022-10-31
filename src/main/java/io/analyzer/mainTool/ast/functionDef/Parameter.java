package io.analyzer.mainTool.ast.functionDef;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.ASTNodeVisitor;
import io.analyzer.mainTool.ast.expressions.Identifier;
import io.analyzer.mainTool.ast.expressions.primaryExpression.PrimaryExpression;

public class Parameter extends ASTNode {
    // 函数形参变量类型
    public ParameterType type = new ParameterType();
    // 函数形参变量名
    public Identifier name = new Identifier();

    public PrimaryExpression defaultValue;

    public void addChild(ASTNode node) {
        if (node instanceof Identifier)
            setName((Identifier) node);
        else if (node instanceof ParameterType)
            setType((ParameterType) node);
        else if (node instanceof PrimaryExpression)
            setDefaultValue((PrimaryExpression) node);
        else
            super.addChild(node);
    }

    public PrimaryExpression getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(PrimaryExpression defaultValue) {
        this.defaultValue = defaultValue;
        super.addChild(defaultValue);
    }

    public void setName(Identifier name) {
        this.name = name;
        super.addChild(name);
    }

    public void setType(ParameterType type) {
        this.type = type;
        super.addChild(type);
    }

    public Identifier getName()
    {
        return name;
    }

    public ParameterType getType()
    {
        return type;
    }

    @Override
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
