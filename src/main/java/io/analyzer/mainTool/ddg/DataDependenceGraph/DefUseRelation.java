package io.analyzer.mainTool.ddg.DataDependenceGraph;

public class DefUseRelation
{
	public Object src;
	public Object dst;
	public String symbol;

	public DefUseRelation(Object aSrc, Object aDst, String aSymbol)
	{
		src = aSrc;
		dst = aDst;
		symbol = aSymbol;
	}

	@Override
	public boolean equals(Object other)
	{
		DefUseRelation otherRel = (DefUseRelation) other;

		return (src == otherRel.src) && (dst == otherRel.dst)
				&& (symbol.equals(otherRel.symbol));
	}

	@Override
	public int hashCode()
	{
		return symbol.hashCode();
	}

}
