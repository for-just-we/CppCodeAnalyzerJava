package io.analyzer.extraTools.utils.sinkPoints;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.expressions.ArrayIndexing;
import io.analyzer.mainTool.ast.expressions.UnaryOperator;
import io.analyzer.mainTool.ast.expressions.binaryExpressions.BinaryExpression;
import io.analyzer.mainTool.ast.expressions.expressionHolders.Callee;

import java.util.Set;

public class SySePoint extends VulDeePoint{
    public Set<String> binOps = Set.of("+", "-", "*", "/");

    public SySePoint(Set<String> sensitive_apis) {
        super(sensitive_apis);
    }

    public boolean judgeSink(ASTNode astNode){
        // Library/API Function Call
        if (astNode instanceof Callee) {
            if (sensitive_apis.contains(astNode.getEscapedCodeStr()))
                return true;
        }
        else if (astNode instanceof ArrayIndexing)
            return true;
        else if (astNode instanceof UnaryOperator) {
            if (astNode.getOperatorCode().equals("*"))
                return true;
        }
        else if (astNode instanceof BinaryExpression)
            if (binOps.contains(astNode.getOperatorCode()))
                return true;

        boolean flag = false;
        for (int i = 0; i < astNode.getChildCount(); ++i)
            flag = flag | judgeSink(astNode.getChild(i));
        return flag;
    }
}
