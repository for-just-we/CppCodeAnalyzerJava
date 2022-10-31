package io.analyzer.mainTool.cdg;

import io.analyzer.mainTool.cfg.CFG;
import io.analyzer.mainTool.cfg.CFGEdge;
import io.analyzer.mainTool.cfg.nodes.CFGNode;
import io.analyzer.mainTool.graphutils.AbstractTwoWayGraph;

// 逆向控制流图
public class ReverseCFG extends AbstractTwoWayGraph<CFGNode, CFGEdge> {
    private CFGNode entry;
    private CFGNode exit;

    private ReverseCFG() {
    }

    public static ReverseCFG newInstance(CFG cfg) {
        ReverseCFG reverseCFG = new ReverseCFG();
        for (CFGNode vertex : cfg.getVertices())
            reverseCFG.addVertex(vertex);

        for (CFGEdge edge : cfg.getEdges()) {
            CFGEdge reverseEdge = new CFGEdge(edge.getDestination(), edge.getSource(), edge.getLabel());
            reverseCFG.addEdge(reverseEdge);
        }
        reverseCFG.entry = cfg.getExitNode();
        reverseCFG.exit = cfg.getEntryNode();
        CFGEdge augmentedEdge = new CFGEdge(reverseCFG.getEntryNode(),
                reverseCFG.getExitNode(), CFGEdge.EMPTY_LABEL);
        reverseCFG.addEdge(augmentedEdge);
        return reverseCFG;
    }

    public CFGNode getEntryNode()
    {
        return entry;
    }

    public CFGNode getExitNode()
    {
        return exit;
    }
}
