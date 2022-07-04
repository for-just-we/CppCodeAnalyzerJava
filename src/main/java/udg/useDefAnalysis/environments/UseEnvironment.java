package udg.useDefAnalysis.environments;

import udg.ASTProvider;
import udg.useDefGraph.UseOrDef;

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
