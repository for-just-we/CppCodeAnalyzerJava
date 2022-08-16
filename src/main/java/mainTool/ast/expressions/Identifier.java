package mainTool.ast.expressions;

import mainTool.ast.ASTNodeVisitor;

public class Identifier extends Expression{

    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }

    public Identifier copy(){
        Identifier identifier = new Identifier();
        identifier.setCodeStr(this.codeStr);
        return identifier;
    }
}
