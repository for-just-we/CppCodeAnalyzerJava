package mainTool.ast.declaration;

import mainTool.ast.ASTNode;
import mainTool.ast.expressions.Identifier;
import mainTool.ast.functionDef.FunctionDef;
import mainTool.ast.statements.Statement;
import mainTool.ast.ASTNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class ClassDefStatement extends Statement {
    public Identifier name; //结构体/类/枚举名称
    public List<FunctionDef> functionDefs = new ArrayList<>(); // 该类中定义的成员函数

    public void addChild(ASTNode expression) {
        if (expression instanceof Identifier)
            name = (Identifier) expression;

        super.addChild(expression);
    }

    public Identifier getName()
    {
        return name;
    }

    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }

    // ClassDef包括结构体，类，枚举和联合体
    enum Type{
        Class("class"),
        Struct("struct"),
        Enum("enum"),
        Union("union");

        private String identifier;

        Type(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }
    }

    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
