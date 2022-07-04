package ast.statements.blockstarters;

import ast.ASTNode;
import ast.expressions.expressionHolders.Condition;
import ast.statements.Statement;

// if, for, while等
public class BlockStarter extends Statement {
    // block
    Statement statement = null;
    // condition
    Condition condition = null;

    public Statement getStatement() {
        return statement;
    }

    public Condition getCondition()
    {
        return condition;
    }

    protected void setStatement(Statement aStatement)
    {
        statement = aStatement;
    }

    public void setCondition(Condition expression)
    {
        condition = expression;
    }

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof Condition)
            setCondition((Condition) node);
        else if (node instanceof Statement)
            setStatement((Statement) node);
        super.addChild(node);
    }
}
