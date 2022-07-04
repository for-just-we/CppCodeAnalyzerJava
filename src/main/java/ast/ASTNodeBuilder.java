package ast;

import antlr.Cpp.CPP14BaseListener;
import org.antlr.v4.runtime.ParserRuleContext;

abstract public class ASTNodeBuilder extends CPP14BaseListener {
    protected ASTNode item;
    public ASTNode getItem() { return item; }

    // abstract public void createNew(ParserRuleContext ctx);
}
