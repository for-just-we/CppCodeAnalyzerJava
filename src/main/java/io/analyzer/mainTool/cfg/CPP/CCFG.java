package io.analyzer.mainTool.cfg.CPP;

import io.analyzer.mainTool.cfg.nodes.CFGNode;
import io.analyzer.mainTool.cfg.CFG;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CCFG extends CFG {
    private List<CFGNode> breakStatements;
    private List<CFGNode> continueStatements;
    private List<CFGNode> returnStatements;
    private HashMap<CFGNode, String> gotoStatements;
    private HashMap<String, CFGNode> labels;

    public CCFG() {
        super();
        setBreakStatements(new LinkedList<>());
        setContinueStatements(new LinkedList<>());
        setReturnStatements(new LinkedList<>());
        setGotoStatements(new HashMap<>());
        setLabels(new HashMap<>());
    }

    public List<CFGNode> getBreakStatements()
    {
        return breakStatements;
    }

    public void setBreakStatements(List<CFGNode> breakStatements)
    {
        this.breakStatements = breakStatements;
    }

    public List<CFGNode> getContinueStatements()
    {
        return continueStatements;
    }


    public void setContinueStatements(List<CFGNode> continueStatements) {
        this.continueStatements = continueStatements;
    }


    public HashMap<String, CFGNode> getLabels() {
        return labels;
    }

    public void setLabels(HashMap<String, CFGNode> labels)
    {
        this.labels = labels;
    }


    public HashMap<CFGNode, String> getGotoStatements()
    {
        return gotoStatements;
    }


    public void setGotoStatements(HashMap<CFGNode, String> gotoStatements)
    {
        this.gotoStatements = gotoStatements;
    }


    public List<CFGNode> getReturnStatements()
    {
        return returnStatements;
    }


    public void setReturnStatements(List<CFGNode> returnStatements)
    {
        this.returnStatements = returnStatements;
    }

    public void addBlockLabel(String label, CFGNode block)
    {
        getLabels().put(label, block);
    }

    public void addBreakStatement(CFGNode statement)
    {
        getBreakStatements().add(statement);
    }

    public void addContinueStatement(CFGNode statement)
    {
        getContinueStatements().add(statement);
    }

    public void addGotoStatement(CFGNode gotoStatement, String gotoTarget)
    {
        getGotoStatements().put(gotoStatement, gotoTarget);
    }

    public void addReturnStatement(CFGNode returnStatement)
    {
        getReturnStatements().add(returnStatement);
    }

    // 记录label(goto用到)对应的CFG结点
    public CFGNode getBlockByLabel(String label)
    {
        CFGNode block = getLabels().get(label);
        if (block == null) {
            System.err.println("warning : can not find block for label "
                    + label);
            return getErrorNode();
        }
        return block;
    }

    @Override
    public void addCFG(CFG o)
    {
        CCFG otherCFG = (CCFG) o;

        super.addCFG(otherCFG);
        // 在之前的基础上合并jump语句。
        getParameters().addAll(otherCFG.getParameters());
        getBreakStatements().addAll(otherCFG.getBreakStatements());
        getContinueStatements().addAll(otherCFG.getContinueStatements());
        getReturnStatements().addAll(otherCFG.getReturnStatements());
        getGotoStatements().putAll(otherCFG.getGotoStatements());
        getLabels().putAll(otherCFG.getLabels());
    }

}
