package ast.statements.blockstarters;

import ast.ASTNode;
import ast.ASTNodeVisitor;

import java.util.Iterator;
import java.util.LinkedList;

public class CatchList extends ASTNode implements Iterable<CatchStatement> {
    private LinkedList<CatchStatement> catchStatements = new LinkedList<>();

    public String getEscapedCodeStr() {
        return "";
    }

    public int size() {
        return this.catchStatements.size();
    }

    public CatchStatement getCatchStatement(int i) {
        return this.catchStatements.get(i);
    }

    public void addCatchStatement(CatchStatement catchStatement) {
        this.catchStatements.add(catchStatement);
        super.addChild(catchStatement);
    }

    @Override
    public Iterator<CatchStatement> iterator() {
        return this.catchStatements.iterator();
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
