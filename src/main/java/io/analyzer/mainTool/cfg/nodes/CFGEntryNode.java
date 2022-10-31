package io.analyzer.mainTool.cfg.nodes;

import io.analyzer.mainTool.databaseNodes.NodeKeys;

import java.util.Map;

public class CFGEntryNode extends AbstractCFGNode{
    @Override
    public String toString()
    {
        return "[ENTRY]";
    }

    @Override
    public Map<String, Object> getProperties()
    {
        Map<String, Object> properties = super.getProperties();
        properties.put(NodeKeys.CODE, "ENTRY");
        return properties;
    }
}
