package extraTools.vuldetect.utils.environments;

import mainTool.udg.ASTProvider;

import java.util.Set;

public class VarDeclEnvironment extends VariableEnvironment{
    private String type;

    public VarDeclEnvironment(ASTProvider astProvider) {
        super(astProvider);
        type = astProvider.getChild(0).getEscapedCodeStr();
    }

    public String getType() {
        return type;
    }

    // 处理子结点的symbol，默认自己解决子结点中的symbol
    public void addChildSymbols(Set<String> childSymbols, ASTProvider child){
        int childNumber = child.getChildNumber();
        // 函数名不添加
        if (childNumber != 0){
            // 参数中的变量名全都处理了
            for (String symbol: childSymbols)
                var2type.put(symbol, type);
            this.handledSymbols.addAll(childSymbols);
        }
    }
}
