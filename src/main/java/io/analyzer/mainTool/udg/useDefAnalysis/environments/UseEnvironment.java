package io.analyzer.mainTool.udg.useDefAnalysis.environments;

import io.analyzer.mainTool.udg.ASTProvider;
import io.analyzer.mainTool.udg.useDefGraph.UseOrDef;

import java.util.Collection;
import java.util.LinkedList;

public class UseEnvironment extends UseDefEnvironment{
    // 只有use，没有def
    public boolean isUse(ASTProvider child)
    {
        return true;
    }

    public Collection<UseOrDef> useOrDefsFromSymbols(ASTProvider child) {
        LinkedList<UseOrDef> retval = new LinkedList<UseOrDef>();
        retval.addAll(createUsesForAllSymbols(symbols));
        return retval;
    }
}
