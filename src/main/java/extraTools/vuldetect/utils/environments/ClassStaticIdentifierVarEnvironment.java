package extraTools.vuldetect.utils.environments;

import mainTool.udg.ASTProvider;

import java.util.Set;

public class ClassStaticIdentifierVarEnvironment extends VariableEnvironment{

    public ClassStaticIdentifierVarEnvironment(ASTProvider astProvider) {
        super(astProvider);
    }

    // 交由父节点处理的symbol
    public Set<String> upstreamSymbols(){
        // Identifier类型直接获取token作为symbol，并返回给父节点处理
        String code = astProvider.getChild(1).getEscapedCodeStr();
        Set<String> retval = Set.of(code);
        return retval;
    }

    // 自己处理的symbol
    public Set<String> selfHandledSymbols(){
        return handledSymbols;
    }
}
