package io.analyzer.extraTools.utils.sinkPoints;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.expressions.expressionHolders.Callee;

import java.util.Set;

public class VulDeePoint {
    public Set<String> sensitive_apis;

    public VulDeePoint(Set<String> sensitive_apis) {
        this.sensitive_apis = sensitive_apis;
    }

    public boolean judgeSink(ASTNode astNode){
        // Library/API Function Call
        if (astNode instanceof Callee)
            if (sensitive_apis.contains(astNode.getEscapedCodeStr()))
                return true;

        boolean flag = false;
        for (int i = 0; i < astNode.getChildCount(); ++i)
            flag = flag | judgeSink(astNode.getChild(i));
        return flag;
    }
}
