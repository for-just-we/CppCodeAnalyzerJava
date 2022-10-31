package io.analyzer.mainTool.ast.statements.blockstarters;

import io.analyzer.mainTool.ast.declaration.ForInit;
import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.expressions.Expression;
import io.analyzer.mainTool.ast.expressions.expressionHolders.Condition;
import io.analyzer.mainTool.ast.statements.Statement;
import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class ForStatement extends BlockStarter {
    private ASTNode forInitStatement = null;
    private ASTNode expression = null;

    public ASTNode getForInitStatement()
    {
        return forInitStatement;
    }

    public void setForInitStatement(ASTNode forInitStatement)
    {
        this.forInitStatement = forInitStatement;
    }

    public ASTNode getExpression()
    {
        return expression;
    }

    public void setExpression(ASTNode expression)
    {
        this.expression = expression;
    }

    @Override
    public void addChild(ASTNode node)
    {
        if (node instanceof Condition)
            setCondition((Condition) node);
        else if(node instanceof ForInit)
            setForInitStatement(node);
        else if(node instanceof Expression)
            setExpression(node);
        else if (node instanceof Statement)
            setStatement((Statement) node);
        super.addChild(node);
    }

    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
