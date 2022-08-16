package mainTool.ast.functionDef;

import mainTool.ast.ASTNode;
import mainTool.ast.DummyNameNode;
import mainTool.ast.expressions.Identifier;
import mainTool.ast.statements.CompoundStatement;
import mainTool.ast.ASTNodeVisitor;

public class FunctionDef extends ASTNode {
    public Identifier name = new DummyNameNode();
    public ParameterList parameterList = new ParameterList();
    public ReturnType returnType = new DummyReturnType();
    CompoundStatement content = new CompoundStatement();

    public CompoundStatement getContent()
    {
        return content;
    }

    public void addStatement(ASTNode statement)
    {
        content.addStatement(statement);
    }

    public void addParameter(Parameter aParameter)
    {
        parameterList.addParameter(aParameter);
    }


    public void replaceName(Identifier curName){
       for (int i = 0; i < children.size(); ++i)
           if (children.get(i) == name){
               children.set(i, curName);
               name = curName;
           }
    }


    @Override
    public String getEscapedCodeStr()
    {
        // check if codeStr has already been generated
        if (codeStr != null)
            return codeStr;
        codeStr = getFunctionSignature();
        return codeStr;
    }

    // 函数签名为函数名 + 参数列表
    public String getFunctionSignature()
    {
        String retval = name.getEscapedCodeStr();
        if (getParameterList() != null)
            retval += " (" + getParameterList().getEscapedCodeStr() + ")";
        else
            retval += " ()";
        return retval;
    }

    public void setContent(CompoundStatement functionContent)
    {
        content = functionContent;
        super.addChild(content);
    }

    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }

    public ParameterList getParameterList()
    {
        return parameterList;
    }

    public void setParameterList(ParameterList parameterList)
    {
        this.parameterList = parameterList;
        super.addChild(this.parameterList);
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
        super.addChild(returnType);
    }

    public void setName(Identifier name) {
        this.name = name;
        super.addChild(name);
    }

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof CompoundStatement)
            setContent((CompoundStatement) node);
        else if (node instanceof ParameterList)
            setParameterList((ParameterList) node);
        else if (node instanceof ReturnType)
            setReturnType((ReturnType) node);
        else if (node instanceof Identifier)
            setName((Identifier) node);
        else
            super.addChild(node);
    }
}
