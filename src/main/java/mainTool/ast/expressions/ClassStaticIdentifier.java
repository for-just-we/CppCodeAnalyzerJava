package mainTool.ast.expressions;

import mainTool.ast.ASTNode;

// int testClass::var = 1;
public class ClassStaticIdentifier extends Identifier{
    // 可能为ClassStaicIdentifier也可能为Identifier，对应testClass
    private Identifier className;
    // 对应var
    private Identifier varName;

    public Identifier getClassName() {
        return className;
    }

    public void setClassName(Identifier className) {
        this.className = className;
    }

    public Identifier getVarName() {
        return varName;
    }

    public void setVarName(Identifier varName) {
        this.varName = varName;
    }

    @Override
    public void addChild(ASTNode node) {
        if (node instanceof Identifier){
            if (className == null)
                setClassName((Identifier) node);
            else
                setVarName((Identifier) node);
        }
        super.addChild(node);
    }

    @Override
    public String getEscapedCodeStr() {
        return className.getEscapedCodeStr() + "::" + varName.getEscapedCodeStr();
    }

    public ClassStaticIdentifier copy(){
        ClassStaticIdentifier staticIdentifier = new ClassStaticIdentifier();
        Identifier className = this.className.copy();
        Identifier varName = this.varName.copy();
        staticIdentifier.addChild(className);
        staticIdentifier.addChild(varName);
        return staticIdentifier;
    }
}
