package mainTool.cdg;

import mainTool.cfg.nodes.CFGNode;
import mainTool.graphutils.Edge;

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
