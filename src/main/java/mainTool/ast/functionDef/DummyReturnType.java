package mainTool.ast.functionDef;

public class DummyReturnType extends ReturnType{
    public DummyReturnType()
    {
        super();
    }

    public String getEscapedCodeStr()
    {
        return "void";
    }
}
