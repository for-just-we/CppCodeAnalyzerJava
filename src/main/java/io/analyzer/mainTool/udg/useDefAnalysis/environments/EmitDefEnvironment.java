package io.analyzer.mainTool.udg.useDefAnalysis.environments;

import io.analyzer.mainTool.udg.ASTProvider;
import io.analyzer.mainTool.udg.useDefGraph.UseOrDef;

import java.util.Collection;
import java.util.LinkedList;

public class EmitDefEnvironment extends UseDefEnvironment{
    Collection<String> defSymbols = new LinkedList<>();

    public void addChildSymbols(LinkedList<String> childSymbols, ASTProvider child) {
        if (isDef(child))
            defSymbols.addAll(childSymbols);
        if (isUse(child))
            symbols.addAll(childSymbols);
    }

    public Collection<UseOrDef> useOrDefsFromSymbols(ASTProvider child) {
        LinkedList<UseOrDef> retval = createDefsForAllSymbols(defSymbols);
        retval.addAll(createUsesForAllSymbols(symbols));
        return retval;
    }
}
