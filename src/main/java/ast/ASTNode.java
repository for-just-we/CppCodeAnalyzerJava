package ast;

import ast.walking.ASTNodeVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import ast.expressions.Expression;
import org.antlr.v4.runtime.tree.ParseTree;
import parsing.ParseTreeUtils;

import java.util.LinkedList;

public class ASTNode {
    protected String codeStr = null; // 该node对应的文本
    public ParserRuleContext parseTreeNodeContext;
    private CodeLocation location = new CodeLocation();

    private boolean isInCFG = false; // 属于CFG node还是纯粹是AST node

    protected LinkedList<ASTNode> children;
    protected int childNumber; // 表示该结点是父节点的第几个child


    public void addChild(ASTNode node)
    {
        if (children == null)
            children = new LinkedList<ASTNode>();
        node.setChildNumber(children.size());
        children.add(node);
    }

    public int getChildCount()
    {
        if (children == null)
            return 0;
        return children.size();
    }

    public ASTNode getChild(int i)
    {
        if (children == null)
            return null;

        ASTNode retval;
        try {
            retval = children.get(i);
        }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
        return retval;
    }

    public ASTNode popLastChild()
    {
        return children.removeLast();
    }

    public void setChildNumber(int num)
    {
        childNumber = num;
    }

    public int getChildNumber()
    {
        return childNumber;
    }

    public void initializeFromContext(ParserRuleContext ctx) {
        parseTreeNodeContext = ctx;
    }

    public void setLocation(ParserRuleContext ctx)
    {
        if (ctx == null)
            return;
        location = new CodeLocation(ctx);
    }

    public void setCodeStr(String aCodeStr)
    {
        codeStr = aCodeStr;
    }


    public String getEscapedCodeStr()
    {
        if (codeStr != null)
            return codeStr;

        codeStr = escapeCodeStr(ParseTreeUtils.childTokenString(parseTreeNodeContext));
        return codeStr;
    }

    private String escapeCodeStr(String codeStr)
    {
        String retval = codeStr;
        retval = retval.replace("\n", "\\n");
        retval = retval.replace("\t", "\\t");
        return retval;
    }

    public String getLocationString()
    {
        setLocation(parseTreeNodeContext);
        return location.toString();
    }

    // 获取该ASTNode对应的代码位置
    public CodeLocation getLocation()
    {
        setLocation(parseTreeNodeContext);
        return location;
    }

    // visitor
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

    // 是否是终端结点
    public boolean isLeaf()
    {
        return (children.size() == 0);
    }

    // 获取AST结点类型
    public String getTypeAsString()
    {
        return this.getClass().getSimpleName();
    }

    // 设置该结点为CFG结点
    public void markAsCFGNode()
    {
        isInCFG = true;
    }

    public boolean isInCFG()
    {
        return isInCFG;
    }

    public String getOperatorCode() {
        // 该ASTNode对应子类是否继承自Expression，也就是该对象是否为Expression
        if (Expression.class.isAssignableFrom(this.getClass()))
            return ((Expression) this).getOperator();
        return null;
    }
}
