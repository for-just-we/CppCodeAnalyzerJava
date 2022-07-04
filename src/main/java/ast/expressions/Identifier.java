package ast.expressions;

import ast.walking.ASTNodeVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Identifier extends Expression{
    public ParserRuleContext getParseTreeNodeContext()
    {
        return parseTreeNodeContext;
    }

    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
