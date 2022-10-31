package io.analyzer.mainTool.cdg;

import io.analyzer.mainTool.cfg.CFG;
import io.analyzer.mainTool.cfg.nodes.CFGNode;

// 构建控制依赖图
public class CDGCreator {
    public CDG create(CFG cfg) {
        // 建立逆向CFG
        ReverseCFG reverseCFG;
        reverseCFG = ReverseCFG.newInstance(cfg);
        // 根据逆向CFG构建支配树
        DominatorTree<CFGNode> dominatorTree;
        dominatorTree = DominatorTree.newInstance(reverseCFG, reverseCFG.getEntryNode());
        // 基于支配树创建CDG
        return CDG.newInstance(dominatorTree);
    }
}
