package mainTool.udg.useDefAnalysis.environments;

import mainTool.udg.ASTProvider;

public class AssignmentEnvironment extends EmitDefEnvironment{
    @Override
    public boolean isUse(ASTProvider child) {
        int childNum = child.getChildNumber();
        // 如果是第一个symbol
        if (childNum == 0) {
            // 如果operator不为空 并且 operator不是 =，也就是 x = y没有使用x，而x += y即使用也重新定义
            String operatorCode = astProvider.getOperatorCode();
            if (operatorCode != null && !operatorCode.equals("="))
                return true;
            else
                return false;
        }
        return true;
    }

    // Assignment Expr中第一个symbol为重新定义，后面的均不是
    @Override
    public boolean isDef(ASTProvider child) {
        int childNum = child.getChildNumber();
        if (childNum == 0)
            return true;
        return false;
    }
}
