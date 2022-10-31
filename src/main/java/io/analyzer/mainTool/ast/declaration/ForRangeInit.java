package io.analyzer.mainTool.ast.declaration;

import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.ast.expressions.Expression;
import io.analyzer.mainTool.ast.expressions.Identifier;

// 相当于IdentifierDecl
public class ForRangeInit extends IdentifierDecl {
    // 要遍历的数组表达式
    private Expression arrayExpr;

    public void addChild(ASTNode node)
    {
        // Type var1: var2
        if (node instanceof Identifier){
            // var1
            if (this.getName() == null)
                setName((Identifier) node);
            // var2
            else
                setArrayExpr((Identifier) node);
        }

        else if (node instanceof Expression)
            setArrayExpr((Expression) node);


        else if (node instanceof IdentifierDeclType)
            setType((IdentifierDeclType) node);

        // super.addChild(node);
    }

    public Expression getArrayExpr() {
        return arrayExpr;
    }

    public void setArrayExpr(Expression arrayExpr) {
        this.arrayExpr = arrayExpr;
        arrayExpr.setChildNumber(children.size());
        children.add(arrayExpr);
    }

    @Override
    public String getEscapedCodeStr() {
        return getType().getEscapedCodeStr() + " " + getName().getEscapedCodeStr() + " : " + arrayExpr.getEscapedCodeStr();
    }
}
