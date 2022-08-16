package mainTool.cfg;

import mainTool.ast.functionDef.FunctionDef;
import mainTool.cfg.CPP.CCFGFactory;

public class ASTToCFGConverter {
    public CFG convert(FunctionDef node)
    {
        // currently, we just use the C-CFG-factory.
        // In the future, this is where we can choose
        // the factory based on the language.
        CCFGFactory factory = new CCFGFactory();
        return factory.newInstance(node);
    }
}
