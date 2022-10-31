package io.analyzer.extraTools.utils;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.expressions.postfixExpressions.CallExpression;

public class CallExprTool {
    public String functionName;
    public int argNum = -1;

    public void judgeCall(ASTNode astNode){
        // Library/API Function Call
        if (astNode instanceof CallExpression){
            functionName = astNode.getChild(0).getEscapedCodeStr();
            argNum = ((CallExpression) astNode).getArgumentList().getChildCount();
            return;
        }
        for (int i = 0; i < astNode.getChildCount(); ++i)
            judgeCall(astNode.getChild(i));
    }
}
