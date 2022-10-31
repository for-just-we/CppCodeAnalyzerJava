package io.analyzer.mainTool.ast;

import io.analyzer.mainTool.antlr.Cpp.CPP14BaseListener;

abstract public class ASTNodeBuilder extends CPP14BaseListener {
    protected ASTNode item;
    public ASTNode getItem() { return item; }

    // abstract public void createNew(ParserRuleContext ctx);
}
