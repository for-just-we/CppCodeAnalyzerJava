package io.analyzer.mainTool.cfg.CPP;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.ASTNodeVisitor;
import io.analyzer.mainTool.ast.statements.CompoundStatement;
import io.analyzer.mainTool.ast.statements.Label;
import io.analyzer.mainTool.ast.statements.blockstarters.*;
import io.analyzer.mainTool.ast.statements.jump.BreakStatement;
import io.analyzer.mainTool.ast.statements.jump.ContinueStatement;
import io.analyzer.mainTool.ast.statements.jump.GotoStatement;
import io.analyzer.mainTool.ast.statements.jump.ReturnStatement;
import io.analyzer.mainTool.cfg.nodes.ASTNodeContainer;
import io.analyzer.mainTool.cfg.nodes.CFGNode;
import io.analyzer.mainTool.ast.functionDef.Parameter;
import io.analyzer.mainTool.ast.functionDef.ParameterList;
import io.analyzer.mainTool.cfg.CFG;

public class StructuredFlowVisitor extends ASTNodeVisitor {
    private CFG returnCFG;

    CFG getCFG() {
        return returnCFG;
    }

    public void visit(ParameterList paramList)
    {
        returnCFG = CCFGFactory.newInstance(paramList);
    }

    public void visit(Parameter param) {
        returnCFG = CCFGFactory.newInstance(param);

        for(CFGNode node : returnCFG.getVertices()){
            if(!(node instanceof ASTNodeContainer))
                continue;
            returnCFG.registerParameter(node);
        }
    }

    public void visit(CompoundStatement content)
    {
        returnCFG = CCFGFactory.newInstance(content);
    }

    public void visit(ASTNode expression) {
        returnCFG = CCFGFactory.newInstance(expression);
    }

    public void visit(ReturnStatement expression)
    {
        returnCFG = CCFGFactory.newInstance(expression);
    }

    public void visit(GotoStatement expression)
    {
        returnCFG = CCFGFactory.newInstance(expression);
    }

    public void visit(IfStatement node)
    {
        returnCFG = CCFGFactory.newInstance(node);
    }

    public void visit(ForStatement node)
    {
        returnCFG = CCFGFactory.newInstance(node);
    }

    public void visit(ForRangeStatement node) { returnCFG = CCFGFactory.newInstance(node); }

    public void visit(WhileStatement node)
    {
        returnCFG = CCFGFactory.newInstance(node);
    }

    public void visit(DoStatement node)
    {
        returnCFG = CCFGFactory.newInstance(node);
    }

    public void visit(SwitchStatement node)
    {
        returnCFG = CCFGFactory.newInstance(node);
    }

    public void visit(TryStatement node) { returnCFG = CCFGFactory.newInstance(node); }

    public void visit(Label node)
    {
        returnCFG = CCFGFactory.newInstance(node);
    }

    public void visit(ContinueStatement expression)
    {
        returnCFG = CCFGFactory.newInstance(expression);
    }

    public void visit(BreakStatement expression)
    {
        returnCFG = CCFGFactory.newInstance(expression);
    }
}
