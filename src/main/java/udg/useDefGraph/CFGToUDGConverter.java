package udg.useDefGraph;

import ast.ASTNode;
import cfg.CFG;
import cfg.nodes.ASTNodeContainer;
import cfg.nodes.CFGNode;
import udg.ASTNodeASTProvider;
import udg.useDefAnalysis.ASTDefUseAnalyzer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CFGToUDGConverter {
    ASTDefUseAnalyzer astAnalyzer = new ASTDefUseAnalyzer();

    public void setAstAnalyzer(ASTDefUseAnalyzer astAnalyzer) {
        this.astAnalyzer = astAnalyzer;
    }

    // 将CFG转化为UDG
    public UseDefGraph convert(CFG cfg)
    {
        // Incrementally create a UseDefGraph by generating
        // UseOrDefs for each statement separately and adding those
        // to the UseDefGraph
        UseDefGraph useDefGraph = new UseDefGraph();
        List<CFGNode> statements = cfg.getVertices();

        // CFG中每个语句独立分析
        for (CFGNode cfgNode : statements) {
            // skip empty blocks
            if (cfgNode instanceof ASTNodeContainer) {
                ASTNode statementNode = ((ASTNodeContainer) cfgNode).getASTNode();
                ASTNodeASTProvider provider = new ASTNodeASTProvider();
                provider.setNode(statementNode);
                Collection<UseOrDef> usesAndDefs = astAnalyzer.analyzeAST(provider);
                addToUseDefGraph(useDefGraph, usesAndDefs, statementNode);
            }
        }

        return useDefGraph;
    }
    // statementNode是CFGNode
    private void addToUseDefGraph(UseDefGraph useDefGraph, Collection<UseOrDef> usesAndDefs, ASTNode statementNode) {
        HashSet<String> insertedForStatementDef = new HashSet<>();
        HashSet<String> insertedForStatementUse = new HashSet<>();

        for (UseOrDef useOrDef : usesAndDefs)
        {
            ASTNodeASTProvider astProvider = (ASTNodeASTProvider) useOrDef.astProvider;
            // CHECK?
            ASTNode useOrDefNode = astProvider.getASTNode();
            if (useOrDef.isDef) {
                if (!insertedForStatementDef.contains(useOrDef.symbol)) {
                    useDefGraph.addDefinition(useOrDef.symbol, statementNode);
                    insertedForStatementDef.add(useOrDef.symbol);
                }
                // 给ASTNode添加
                if (useOrDefNode != null && useOrDefNode != statementNode)
                    useDefGraph.addDefinition(useOrDef.symbol, useOrDefNode);
            }
            else {

                if (!insertedForStatementUse.contains(useOrDef.symbol)) {
                    useDefGraph.addUse(useOrDef.symbol, statementNode);
                    insertedForStatementUse.add(useOrDef.symbol);
                }

                // Add use-links from AST nodes to symbols
                if (useOrDef.astProvider != null && useOrDefNode != statementNode)
                    useDefGraph.addUse(useOrDef.symbol, useOrDefNode);
            }
        }
    }
}
