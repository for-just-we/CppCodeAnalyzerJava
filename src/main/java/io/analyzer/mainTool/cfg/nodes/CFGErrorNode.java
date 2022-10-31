package io.analyzer.mainTool.cfg.nodes;

import io.analyzer.mainTool.databaseNodes.NodeKeys;

import java.util.Map;

public class CFGErrorNode extends AbstractCFGNode{
    @Override
    public String toString()
    {
        return "[ERROR]";
    }

    @Override
    public Map<String, Object> getProperties()
    {
        Map<String, Object> properties = super.getProperties();
        properties.put(NodeKeys.CODE, "ERROR");
        return properties;
    }
}
