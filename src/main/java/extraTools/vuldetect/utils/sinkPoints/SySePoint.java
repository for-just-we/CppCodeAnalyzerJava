package extraTools.vuldetect.utils.sinkPoints;

import mainTool.ast.ASTNode;
import mainTool.ast.expressions.ArrayIndexing;
import mainTool.ast.expressions.UnaryOp;
import mainTool.ast.expressions.UnaryOperator;
import mainTool.ast.expressions.binaryExpressions.BinaryExpression;
import mainTool.ast.expressions.expressionHolders.Callee;

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
