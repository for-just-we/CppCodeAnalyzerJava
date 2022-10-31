package io.analyzer.mainTool.udg.useDefAnalysis.environments;

import io.analyzer.mainTool.udg.ASTProvider;

public class DeclEnvironment extends EmitDefEnvironment{
    @Override
    public boolean isUse(ASTProvider child)
    {
        return false;
    }

    @Override
    public boolean isDef(ASTProvider child) {
        String type = child.getTypeAsString();
        int childNum = child.getChildNumber();
        // IdentifierDecl的子结点第0个是IdentifierDeclType，第1个是Identifier
        return (childNum == 1 && type.equals("Identifier"));
    }
}
