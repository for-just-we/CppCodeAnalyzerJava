package io.analyzer.extraTools.utils.environments;

import io.analyzer.mainTool.udg.ASTProvider;

import java.util.HashSet;
import java.util.Set;

public class CallVarEnvironment extends VariableEnvironment {
    public CallVarEnvironment(ASTProvider astProvider) {
        super(astProvider);
    }

    public void addChildSymbols(Set<String> childSymbols, ASTProvider child){
        int childNumber = child.getChildNumber();
        // 函数名不添加
        if (childNumber != 0)
            // 参数中的变量名全都处理了
            this.handledSymbols.addAll(childSymbols);
    }

    // 交由父节点处理的symbol
    public Set<String> upstreamSymbols(){
        return new HashSet<>();
    }

    // 自己处理的symbol
    public Set<String> selfHandledSymbols(){
        return new HashSet<>();
    }
}
