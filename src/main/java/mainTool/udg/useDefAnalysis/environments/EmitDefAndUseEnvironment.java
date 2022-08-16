package mainTool.udg.useDefAnalysis.environments;

import mainTool.udg.ASTProvider;
import mainTool.udg.useDefGraph.UseOrDef;

import java.util.Collection;
import java.util.LinkedList;

public class EmitDefAndUseEnvironment extends UseDefEnvironment{
    Collection<String> defSymbols = new LinkedList<>();
    Collection<String> useSymbols = new LinkedList<>();

    public void addChildSymbols(LinkedList<String> childSymbols, ASTProvider child) {
        if (isDef(child))
            defSymbols.addAll(childSymbols);
        if (isUse(child))
            useSymbols.addAll(childSymbols);
    }

    public Collection<UseOrDef> useOrDefsFromSymbols(ASTProvider child) {
        LinkedList<UseOrDef> retval = new LinkedList<>();
        if (isDef(child))
            retval.addAll(createDefsForAllSymbols(defSymbols));
        if (isUse(child))
            retval.addAll(createUsesForAllSymbols(useSymbols));

        return retval;
    }

//    public LinkedList<String> upstreamSymbols()
//    {
//        return emptySymbolList;
//    }
}
