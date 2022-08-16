package mainTool.udg.useDefAnalysis.environments;

import mainTool.udg.ASTProvider;
import mainTool.udg.useDefGraph.UseOrDef;

import java.util.Collection;
import java.util.LinkedList;

public class PtrMemberAccessEnvironment extends UseDefEnvironment{
    @Override
    public LinkedList<String> upstreamSymbols()
    {

        LinkedList<String> retval = new LinkedList<String>();

        // emit all symbols as '* symbol'

        LinkedList<String> derefedChildren = new LinkedList<String>();
        for(String c : symbols){
            derefedChildren.add("* " + c);
        }

        retval.addAll(derefedChildren);

        // emit entire code string
        String codeStr = astProvider.getEscapedCodeStr();
        retval.add(codeStr);

        return retval;
    }

    @Override
    public void addChildSymbols(LinkedList<String> childSymbols,
                                ASTProvider child)
    {
        int childNum = child.getChildNumber();
        // Only add the left child but never the right child
        if (childNum == 0)
            super.addChildSymbols(childSymbols, child);
    }

    @Override
    public Collection<UseOrDef> useOrDefsFromSymbols(ASTProvider child)
    {
        return createUsesForAllSymbols(symbols);
    }
}
