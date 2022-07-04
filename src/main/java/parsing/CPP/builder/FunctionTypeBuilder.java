package parsing.CPP.builder;

import antlr.Cpp.CPP14Parser;
import ast.ASTNodeBuilder;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Stack;


// 这个类用来解析函数返回基础类型
public class FunctionTypeBuilder extends ASTNodeBuilder {
    private String type = "";
    private Stack<String> idType = new Stack<>();
    private final String varDecl = "varDecl";

    public String getType() {
        return type;
    }

    @Override
    public void enterOtherDecl(CPP14Parser.OtherDeclContext ctx) {
        idType.push(varDecl);
    }

    @Override
    public void exitOtherDecl(CPP14Parser.OtherDeclContext ctx) {
        idType.pop();
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        if (!idType.empty() && idType.peek().equals(varDecl))
            type += node.getText() + " ";
    }
}
