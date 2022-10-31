package io.analyzer.mainTool.ast;

import io.analyzer.mainTool.ast.expressions.Identifier;

public class DummyNameNode extends Identifier {
    public DummyNameNode() {
        super();
    }

    public String getEscapedCodeStr()
    {
        return "<unnamed>";
    }
}
