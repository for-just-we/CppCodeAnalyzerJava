package io.analyzer.mainTool.cfg.nodes;

import io.analyzer.mainTool.databaseNodes.NodeKeys;

import java.util.Map;

public class EmptyBlock extends AbstractCFGNode{
    @Override
    public String toString()
    {
        return "[EMPTY BLOCK]";
    }

    @Override
    public Map<String, Object> getProperties()
    {
        Map<String, Object> properties = super.getProperties();
        properties.put(NodeKeys.CODE, "EMPTY BLOCK");
        return properties;
    }
}
