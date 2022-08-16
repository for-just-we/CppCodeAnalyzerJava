package mainTool.udg.useDefAnalysis.environments;

import mainTool.udg.ASTProvider;

// x++, x--
public class IncDecEnvironment extends EmitDefEnvironment{
    @Override
    public boolean isUse(ASTProvider child)
    {
        return true;
    }

    @Override
    public boolean isDef(ASTProvider child)
    {
        int childNum = child.getChildNumber();

        if (childNum == 0)
            return true;
        return false;
    }
}
