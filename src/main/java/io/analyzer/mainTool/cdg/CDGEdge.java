package io.analyzer.mainTool.cdg;

import io.analyzer.mainTool.cfg.nodes.CFGNode;
import io.analyzer.mainTool.graphutils.Edge;

import java.util.Map;

// 控制依赖边，连接CFG结点
public class CDGEdge extends Edge<CFGNode> {
    public CDGEdge(CFGNode source, CFGNode destination)
    {
        super(source, destination);
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }
}
