package mainTool.cfg.CPP;

import mainTool.ast.ASTNode;
import mainTool.ast.functionDef.Parameter;
import mainTool.ast.functionDef.ParameterList;
import mainTool.ast.statements.CompoundStatement;
import mainTool.ast.statements.Label;
import mainTool.ast.statements.blockstarters.*;
import mainTool.ast.statements.jump.BreakStatement;
import mainTool.ast.statements.jump.ContinueStatement;
import mainTool.ast.statements.jump.GotoStatement;
import mainTool.ast.statements.jump.ReturnStatement;
import mainTool.ast.ASTNodeVisitor;
import mainTool.cfg.CFG;
import mainTool.cfg.nodes.ASTNodeContainer;
import mainTool.cfg.nodes.CFGNode;

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
