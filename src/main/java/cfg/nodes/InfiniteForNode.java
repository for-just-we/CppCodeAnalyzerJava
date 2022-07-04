package cfg.nodes;

import databaseNodes.NodeKeys;

import java.util.Map;

// for循环条件没写的话就是InfiniteForNode
public class InfiniteForNode extends AbstractCFGNode{
    @Override
    public String toString()
    {
        return "[INFINITE FOR]";
    }

    @Override
    public Map<String, Object> getProperties()
    {
        Map<String, Object> properties = super.getProperties();
        properties.put(NodeKeys.CODE, "true");
        return properties;
    }
}
