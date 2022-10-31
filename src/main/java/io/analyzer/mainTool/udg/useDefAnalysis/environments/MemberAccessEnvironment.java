package io.analyzer.mainTool.udg.useDefAnalysis.environments;

import io.analyzer.mainTool.udg.ASTProvider;
import io.analyzer.mainTool.udg.useDefGraph.UseOrDef;

import java.util.Collection;
import java.util.LinkedList;

// 结构体成员访问
public class MemberAccessEnvironment extends UseDefEnvironment{

    @Override
    public LinkedList<String> upstreamSymbols() {
        LinkedList<String> retval = new LinkedList<>();
        // emit all symbols
        retval.addAll(symbols);

        // emit entire code string
        String codeStr = astProvider.getEscapedCodeStr();
        retval.add(codeStr);

        return retval;
    }

    @Override
    public void addChildSymbols(LinkedList<String> childSymbols, ASTProvider child) {
        int childNum = child.getChildNumber();
        // Only add the left child but never the right child
        // 结构体变量名添加到symbol中但是使用的成员变量名不添加
        if (childNum == 0)
            super.addChildSymbols(childSymbols, child);
    }

    @Override
    public Collection<UseOrDef> useOrDefsFromSymbols(ASTProvider child)
    {
        return createUsesForAllSymbols(symbols);
    }
}
