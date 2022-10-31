package io.analyzer.extraTools.utils.environments;

import io.analyzer.mainTool.udg.ASTProvider;

import java.util.Set;

public class MemberAccessVarEnvironment extends VariableEnvironment{
    public MemberAccessVarEnvironment(ASTProvider astProvider) {
        super(astProvider);
    }

    // 交由父节点处理的symbol
    public Set<String> upstreamSymbols(){
        // Identifier类型直接获取token作为symbol，并返回给父节点处理
        String code = astProvider.getEscapedCodeStr();
        Set<String> retval = Set.of(code);
        return retval;
    }

    // 处理子结点的symbol，默认自己解决子结点中的symbol
    public void addChildSymbols(Set<String> childSymbols, ASTProvider child){
        int childNumber = child.getChildNumber();
        // 函数名不添加
        if (childNumber != 0)
            // 参数中的变量名全都处理了
            this.handledSymbols.addAll(childSymbols);
    }
}
