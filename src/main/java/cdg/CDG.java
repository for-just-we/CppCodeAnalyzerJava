package cdg;

import cfg.nodes.CFGEntryNode;
import cfg.nodes.CFGNode;
import graphutils.AbstractGraph;

import java.util.Set;

// 控制依赖图定义
public class CDG extends AbstractGraph<CFGNode, CDGEdge> {
    private DominatorTree<CFGNode> dominatorTree;

    private CDG() {
    }

    public DominatorTree<CFGNode> getDominatorTree()
    {
        return this.dominatorTree;
    }

    // 根据支配树建立控制依赖图
    public static CDG newInstance(DominatorTree<CFGNode> dominatorTree) {
        CDG cdg = new CDG();
        cdg.dominatorTree = dominatorTree;
        for (CFGNode vertex : dominatorTree.getVertices()) {
            Set<CFGNode> frontier = dominatorTree.dominanceFrontier(vertex);
            if (frontier != null) {
                cdg.addVertex(vertex);
                for (CFGNode f : frontier) {
                    if (f == vertex || f instanceof CFGEntryNode)
                        continue;
                    cdg.addVertex(f);
                    cdg.addEdge(new CDGEdge(f, vertex));
                }
            }
        }
        return cdg;
    }
}
