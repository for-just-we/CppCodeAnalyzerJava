package cfg.nodes;

import ast.ASTNode;
import ast.CodeLocation;

public class ASTNodeContainer extends AbstractCFGNode implements Comparable{
    private ASTNode astNode;

    public ASTNodeContainer(ASTNode node) {
        if (node == null)
            throw new IllegalArgumentException("node must not be null");
        setASTNode(node);
    }

    private void setASTNode(ASTNode astNode) {
        this.astNode = astNode;
        this.astNode.markAsCFGNode();
    }

    public ASTNode getASTNode()
    {
        return astNode;
    }

    public String getEscapedCodeStr() {
        if (getASTNode() == null)
            return "";
        return getASTNode().getEscapedCodeStr();
    }

    public void markAsCFGNode() {
        if (getASTNode() == null)
            return;
        getASTNode().markAsCFGNode();
    }

    @Override
    public String toString()
    {
        return "[" + astNode.getEscapedCodeStr() + "]";
    }

    @Override
    public int compareTo(Object o) {
        ASTNodeContainer node = (ASTNodeContainer) o;
        return this.getASTNode().getLocation().compareTo(node.getASTNode().getLocation());
    }
}
