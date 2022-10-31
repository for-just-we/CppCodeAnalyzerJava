package io.analyzer.mainTool.udg.useDefAnalysis.environments;

import io.analyzer.mainTool.udg.ASTProvider;

import java.util.LinkedList;

public class UnaryOpEnvironment extends EmitUseEnvironment{
    public void addChildSymbols(LinkedList<String> childSymbols, ASTProvider child) {
        String codeStr = astProvider.getEscapedCodeStr();
        if(codeStr != null && codeStr.startsWith("&")){
            for(String symbol : childSymbols)
                symbols.add("& " + symbol);
            return;
        }
        // 不是*p
        if(codeStr == null || !codeStr.startsWith("*")){
            symbols.addAll(childSymbols);
            return;
        }

        LinkedList<String> retval = new LinkedList<>();
        // emit all symbols as '* symbol'
        LinkedList<String> derefedChildren = new LinkedList<>();
        for(String c : childSymbols)
            derefedChildren.add("* " + c);
        retval.addAll(derefedChildren);
        // emit entire code string
        //retval.add(codeStr);
        useSymbols.addAll(childSymbols);
        symbols.addAll(retval);

    }
}
