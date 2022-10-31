package io.analyzer.mainTool.cfg;

import io.analyzer.mainTool.cfg.CPP.CCFGFactory;
import io.analyzer.mainTool.ast.functionDef.FunctionDef;

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
