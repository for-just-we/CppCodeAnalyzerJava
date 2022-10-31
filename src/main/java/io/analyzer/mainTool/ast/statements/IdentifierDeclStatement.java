package io.analyzer.mainTool.ast.statements;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.ASTNodeVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public class IdentifierDeclStatement extends Statement{
    ParserRuleContext typeNameContext;

    public List<ASTNode> getIdentifierDeclList()
    {
        return children;
    }

    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }

    public ParserRuleContext getTypeNameContext()
    {
        return typeNameContext;
    }

    public void setTypeNameContext(ParserRuleContext ctx)
    {
        typeNameContext = ctx;
    }
}
