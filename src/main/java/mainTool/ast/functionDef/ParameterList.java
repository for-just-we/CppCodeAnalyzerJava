package mainTool.ast.functionDef;

import mainTool.ast.ASTNode;
import mainTool.ast.expressions.Identifier;
import mainTool.ast.ASTNodeVisitor;

import java.util.Iterator;
import java.util.LinkedList;

public class ParameterList extends ASTNode {

    private LinkedList<Parameter> parameters = new LinkedList<Parameter>();
    // TODO: we don't want to give back a reference to the list,
    // we need to provide iterators for type and name
    public LinkedList<Parameter> getParameters()
    {
        return parameters;
    }

    public void addParameter(Parameter aParam)
    {
        parameters.add(aParam);
        super.addChild(aParam);
    }

    // 获取所有参数名字
    public Identifier[] getNames()
    {
        Identifier retNames[] = new Identifier[parameters.size()];
        for (int i = 0; i < parameters.size(); i++)
            retNames[i] = parameters.get(i).name;
        return retNames;
    }

    // 获取参数名字的string
    public String[] getNameStrings()
    {
        String retStrings[] = new String[parameters.size()];
        for (int i = 0; i < parameters.size(); i++)
            retStrings[i] = parameters.get(i).name.getEscapedCodeStr();
        return retStrings;
    }

    // 获取所有参数对应的类型
    public ParameterType[] getTypes()
    {
        ParameterType retTypes[] = new ParameterType[parameters.size()];
        for (int i = 0; i < parameters.size(); i++)
            retTypes[i] = parameters.get(i).type;
        return retTypes;
    }

    //  获取所有参数对应类型的string
    public String[] getTypeStrings()
    {
        String retStrings[] = new String[parameters.size()];
        for (int i = 0; i < parameters.size(); i++)
            retStrings[i] = parameters.get(i).type.getEscapedCodeStr();
        return retStrings;
    }


    // 将所有参数名加载到一个string里面
    @Override
    public String getEscapedCodeStr()
    {
        if (codeStr != null)
            return codeStr;

        if (parameters.size() == 0) {
            codeStr = "";
            return codeStr;
        }

        Iterator<Parameter> i = parameters.iterator();
        StringBuilder s = new StringBuilder();
        for (; i.hasNext();) {
            Parameter param = i.next();
            s.append(param.getEscapedCodeStr() + " , ");
        }

        codeStr = s.toString();
        codeStr = codeStr.substring(0, s.length() - 3);

        return codeStr;
    }

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof Parameter)
            addParameter((Parameter) node);
    }

    @Override
    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
