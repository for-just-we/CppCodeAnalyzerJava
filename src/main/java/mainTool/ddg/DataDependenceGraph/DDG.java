package mainTool.ddg.DataDependenceGraph;

import java.util.HashSet;
import java.util.Set;

public class DDG {
	private Set<DefUseRelation> defUseEdges = new HashSet<>();

	public Set<DefUseRelation> getDefUseEdges()
	{
		return defUseEdges;
	}

	public void add(Object srcId, Object dstId, String symbol) {
		DefUseRelation statementPair = new DefUseRelation(srcId, dstId, symbol);
		defUseEdges.add(statementPair);
	}
}
