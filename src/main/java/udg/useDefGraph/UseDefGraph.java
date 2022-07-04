package udg.useDefGraph;

import ast.ASTNode;
import misc.MultiHashMap;

import java.util.List;

public class UseDefGraph {
    // A UseDefGraph is a table indexed
    // by identifiers. Each table-entry
    // is a list of the UseOrDefRecords
    // of the identifier.

    // 主要成员变量，每个key对应1个list [(stetement, def)]
    MultiHashMap<String, UseOrDefRecord> useOrDefRecordTable = new MultiHashMap<String, UseOrDefRecord>();

    public MultiHashMap<String, UseOrDefRecord> getUseDefDict()
    {
        return useOrDefRecordTable;
    }

    public List<UseOrDefRecord> getUsesAndDefsForSymbol(String symbol)
    {
        return useOrDefRecordTable.get(symbol);
    }

    public void addDefinition(String identifier, ASTNode astNode)
    {
        add(identifier, astNode, true);
    }

    public void addUse(String identifier, ASTNode astNode)
    {
        add(identifier, astNode, false);
    }

    private void add(String identifier, ASTNode astNode, boolean isDef) {
        UseOrDefRecord record = new UseOrDefRecord(astNode, isDef);
        useOrDefRecordTable.add(identifier, record);
    }
}
