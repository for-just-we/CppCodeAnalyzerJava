package io.analyzer.extraTools.utils.environments;

import io.analyzer.mainTool.udg.ASTProvider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VariableEnvironment {
    public ASTProvider astProvider;
    public Set<String> handledSymbols = new HashSet<>(); // 自己处理的token
    public Set<String> upStreamSymbols = new HashSet<>(); // 交由父节点处理的token
    public Map<String, String> var2type = new HashMap<>(); // 变量名映射为类型名
    public Set<String> funcNames = new HashSet<>(); // 使用过的函数名

    public VariableEnvironment(ASTProvider astProvider) {
        this.astProvider = astProvider;
    }

    // 处理子结点的symbol，默认自己解决子结点中的symbol
    public void addChildSymbols(Set<String> childSymbols, ASTProvider child){
        handledSymbols.addAll(childSymbols);
    }

    // 交由父节点处理的symbol
    public Set<String> upstreamSymbols(){
        return upStreamSymbols;
    }

    // 自己处理的symbol
    public Set<String> selfHandledSymbols(){
        return handledSymbols;
    }
}
