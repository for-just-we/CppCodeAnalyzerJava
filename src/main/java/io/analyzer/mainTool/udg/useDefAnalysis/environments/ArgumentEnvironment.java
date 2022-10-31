package io.analyzer.mainTool.udg.useDefAnalysis.environments;

import io.analyzer.mainTool.udg.ASTProvider;

import java.util.LinkedList;

public class ArgumentEnvironment extends EmitDefAndUseEnvironment{
    // 该参数是否会使用指针
    boolean isUsePointer = false;
    // 是否重新定义指针
    boolean isDefPointer = false;

    @Override
    public void addChildSymbols(LinkedList<String> childSymbols, ASTProvider child) {
        // 函数调用默认不会改变参数，参数指针指向可能改变
        if (isDefPointer){
            // For tainted arguments, add "* symbol" instead of symbol
            // to defined symbols. Make an exception if symbol starts with '& '
            LinkedList<String> derefChildSymbols = new LinkedList<>();
            for(String symbol : childSymbols){
                if(!symbol.startsWith("& "))
                    derefChildSymbols.add("* " + symbol);
                else
                    derefChildSymbols.add(symbol.substring(2));
            }
            defSymbols.addAll(derefChildSymbols);
        }
        if (isUsePointer){
            // 使用了指针类型，那么 * + id 会被添加到使用到的symbol中
            LinkedList<String> derefChildSymbols = new LinkedList<>();
            for(String symbol : childSymbols){
                if(!symbol.startsWith("& "))
                    derefChildSymbols.add("* " + symbol);
                    // !patch to see if we can detect macro-sources!
                    // derefChildSymbols.add(symbol);
                else
                    derefChildSymbols.add(symbol.substring(2));
            }
            useSymbols.addAll(derefChildSymbols);
        }
        // id 会被添加到使用到的symbol中
        useSymbols.addAll(childSymbols);
    }

    @Override
    public boolean isUse(ASTProvider child)
    {
        return true;
    }

    @Override
    public boolean isDef(ASTProvider child) {
        return isDefPointer;
    }

    public void setIsUsePointer()
    {
        isUsePointer = true;
    }

    public void setIsDefPointer()
    {
        isDefPointer = true;
    }
}
