package io.analyzer.extraTools.utils.environments;

import io.analyzer.mainTool.udg.ASTProvider;

import java.util.Set;

public class CalleeEnvironment extends VariableEnvironment{
    public CalleeEnvironment(ASTProvider astProvider) {
        super(astProvider);
    }

    // 处理子结点的symbol，默认自己解决子结点中的symbol
    public void addChildSymbols(Set<String> childSymbols, ASTProvider child){
        funcNames.addAll(childSymbols);
    }
}
