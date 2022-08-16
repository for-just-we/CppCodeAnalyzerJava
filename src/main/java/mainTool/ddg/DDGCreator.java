package mainTool.ddg;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import mainTool.cfg.nodes.CFGExitNode;
import mainTool.ddg.DefUseCFG.DefUseCFG;
import mainTool.miscs.HashMapOfSets;
import mainTool.ddg.DataDependenceGraph.DDG;

public class DDGCreator {
	DefUseCFG cfg;
	HashMapOfSets in = new HashMapOfSets(); // in集合
	HashMapOfSets out = new HashMapOfSets(); // out集合
	HashMapOfSets gen = new HashMapOfSets(); // gen集合
	HashSet<Object> changedNodes;

	public void clear(){
		in.clear();
		out.clear();
		gen.clear();
	}

	private class Definition {
		public Definition(Object aStatement, String aIdentifier)
		{
			statement = aStatement;
			identifier = aIdentifier;
		}

		public Object statement;
		public String identifier;
	};


	public DDG createForDefUseCFG(DefUseCFG aCfg) {
		cfg = aCfg;
		calculateReachingDefs();
		return createDDGFromReachingDefs();
	}

	// Reaching Def Analysis
	private void calculateReachingDefs() {
		initReachingDefs();

		while (!changedNodes.isEmpty()) {
			Object currentBlock = popFromChangedNodes();

			updateIn(currentBlock);
			boolean changed = updateOut(currentBlock);

			if (!changed)
				continue;

			List<Object> children = cfg.getChildBlocks().getListForKey(currentBlock);
			if (children == null)
				continue;

			for (Object o : children)
				changedNodes.add(o);

		}
	}

	// Reaching-Def info初始化
	private void initReachingDefs()
	{
		initOut();
		initGenFromOut();
		changedNodes = new HashSet<>();
		changedNodes.addAll(cfg.getStatements());
	}

	private Object popFromChangedNodes()
	{
		Object x = changedNodes.iterator().next();
		changedNodes.remove(x);
		return x;
	}

	private void initOut()
	{
		for (Object statement : cfg.getStatements())
		{

			// this has the nice side-effect that an
			// empty hash is created for the statement.

			out.removeAllForKey(statement);

			List<Object> symsDefined = cfg.getSymbolsDefined().getListForKey(
					statement);
			if (symsDefined == null)
				continue;

			for (Object s : symsDefined)
			{
				String symbol = (String) s;
				out.add(statement, new Definition(statement, symbol));
			}
		}
	}

	private void initGenFromOut()
	{
		for (Object statement : cfg.getStatements())
		{
			for (Object o : out.getListForKey(statement))
				gen.add(statement, o);
		}
	}

	private void updateIn(Object x)
	{
		List<Object> parents = cfg.getParentBlocks().getListForKey(x);
		if (parents == null)
			return;

		in.removeAllForKey(x);

		// in(x) = union(out(p))_{p in parents(x)}
		for (Object parent : parents)
		{
			HashSet<Object> parentOut = out.getListForKey(parent);
			if (parentOut == null)
				continue;
			for (Object o : parentOut)
				in.add(x, o);
		}
	}

	private boolean updateOut(Object x)
	{
		HashSet<Object> listForKey = out.getListForKey(x);
		HashSet<Object> oldOut = new HashSet<Object>(listForKey);

		out.removeAllForKey(x);

		// in(x)
		HashSet<Object> inForX = in.getListForKey(x);
		if (inForX != null)
		{
			for (Object o : inForX)
			{
				out.add(x, o);
			}
		}

		// -kill(x)
		List<Object> killX = cfg.getSymbolsDefined().getListForKey(x);
		if (killX != null)
		{

			Iterator<Object> it = out.getListForKey(x).iterator();
			while (it.hasNext())
			{
				Definition def = (Definition) it.next();
				if (killX.contains(def.identifier))
					it.remove();
			}

		}

		// gen(X)
		HashSet<Object> genX = gen.getListForKey(x);

		if (genX != null)
		{
			for (Object o : genX)
			{
				out.add(x, o);
			}
		}

		return !oldOut.equals(out.getListForKey(x));
	}

	private DDG createDDGFromReachingDefs()
	{
		DDG ddg = new DDG();

		for (Object statement : cfg.getStatements())
		{
			if (statement instanceof CFGExitNode)
				continue;
			HashSet<Object> inForBlock = in.getListForKey(statement);
			if (inForBlock == null)
				continue;
			List<Object> usedSymbols = cfg.getSymbolsUsed().getListForKey(
					statement);
			if (usedSymbols == null)
				continue;

			for (Object d : inForBlock)
			{
				Definition def = (Definition) d;
				if (def.statement == statement)
					continue;
				if (usedSymbols.contains(def.identifier))
					ddg.add(def.statement, statement, def.identifier);
			}
		}

		return ddg;
	}

}
