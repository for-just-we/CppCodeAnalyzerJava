package io.analyzer.extraTools.utils.environments;

import io.analyzer.mainTool.udg.ASTProvider;

import java.util.Set;

public class IdentifierVarEnvironment extends VariableEnvironment{
    public IdentifierVarEnvironment(ASTProvider astProvider) {
        super(astProvider);
    }

    // 交由父节点处理的symbol
    public Set<String> upstreamSymbols(){
        // Identifier类型直接获取token作为symbol，并返回给父节点处理
        String code = astProvider.getEscapedCodeStr();
        Set<String> retval = Set.of(code);
        return retval;
    }

    // 自己处理的symbol
    public Set<String> selfHandledSymbols(){
        return handledSymbols;
    }
}
