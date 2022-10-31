package io.analyzer.mainTool.ast.declaration;

import io.analyzer.mainTool.ast.ASTNode;

public class IdentifierDeclType extends ASTNode {
    public String baseType;
    public String completeType;

    public String getEscapedCodeStr()
    {
        return completeType;
    }
}
