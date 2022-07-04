package udg.useDefAnalysis.environments;

import udg.ASTProvider;

import java.util.LinkedList;

public class ArrayIndexingEnvironment extends EmitDefAndUseEnvironment{

    @Override
    public void addChildSymbols(LinkedList<String> childSymbols, ASTProvider child) {
        int childNum = child.getChildNumber();
        // 数组名
        if (childNum == 0){
            LinkedList<String> derefedChildren = new LinkedList<>();
            for(String c : childSymbols)
                derefedChildren.add("* " + c);
            symbols.addAll(derefedChildren);
        }
        useSymbols.addAll(childSymbols);
    }

    @Override
    public boolean isUse(ASTProvider child) {
        return true;
    }
}
