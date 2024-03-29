package io.analyzer.mainTool.ast.statements.jump;

import io.analyzer.mainTool.ast.ASTNodeVisitor;

public class GotoStatement extends JumpStatement {
    // 跳转到的label
    public String getTarget() {
        return getChild(0).getEscapedCodeStr();
    }

    public String getEscapedCodeStr() {
        return "goto " + getTarget() + ";";
    }

    public void accept(ASTNodeVisitor visitor)
    {
        visitor.visit(this);
    }
}
