package udg.useDefAnalysis.environments;

import udg.ASTProvider;

import java.util.LinkedList;

public class CallEnvironment extends UseDefEnvironment{
    @Override
    public void addChildSymbols(LinkedList<String> childSymbols, ASTProvider child) {
        int childNumber = child.getChildNumber();
        // 函数名不添加
        if (childNumber != 0)
            symbols.addAll(childSymbols);
    }
}
