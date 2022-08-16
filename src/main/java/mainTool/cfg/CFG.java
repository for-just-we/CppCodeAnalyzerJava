package mainTool.cfg;

import mainTool.cfg.nodes.CFGEntryNode;
import mainTool.cfg.nodes.CFGErrorNode;
import mainTool.cfg.nodes.CFGExitNode;
import mainTool.cfg.nodes.CFGNode;
import mainTool.graphutils.AbstractTwoWayGraph;

import java.util.LinkedList;
import java.util.List;

// 针对一个function的CFG
public class CFG extends AbstractTwoWayGraph<CFGNode, CFGEdge> {
    private CFGEntryNode entry; // function入口点
    private CFGExitNode exit; // function退出点
    private CFGErrorNode error;
    private List<CFGNode> parameters; // 函数参数
    private String name; //函数名

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public CFG() {
        entry = new CFGEntryNode();
        exit = new CFGExitNode();
        addVertex(entry);
        addVertex(exit);
        parameters = new LinkedList<CFGNode>();
    }


    @Override
    public boolean isEmpty()
    {
        // do not count entry and exit node, since they do not provide any
        // additional information.
        return size() == 2;
    }

    public CFGNode getExitNode()
    {
        return exit;
    }

    public CFGNode getEntryNode()
    {
        return entry;
    }

    public CFGNode getErrorNode() {
        if (error == null) {
            error = new CFGErrorNode();
            addVertex(error);
        }
        return error;
    }

    public void registerParameter(CFGNode parameter)
    {
        parameters.add(parameter);
    }

    public List<CFGNode> getParameters()
    {
        return parameters;
    }

    // addCFG只是将otherCFG和当前CFG的所有结点和边放到当前CFG中，otherCFG中的结点还没有和当前CFG连通
    public void addCFG(CFG otherCFG) {
        addVertices(otherCFG);
        addEdges(otherCFG);
    }

    // 合并CFG
    public void appendCFG(CFG otherCFG) {
        addCFG(otherCFG);
        if (!otherCFG.isEmpty()) {
            // edge1为当前CFG的ExitNode的入边
            for (CFGEdge edge1 : ingoingEdges(getExitNode()))
                // edge2为otherCFG的EntryNode的出边
                for (CFGEdge edge2 : otherCFG.outgoingEdges(otherCFG.getEntryNode()))
                    // 将edge1的source和edge2的dest连接起来
                    addEdge(edge1.getSource(), edge2.getDestination(),edge1.getLabel());
            // 删除当前的Exit结点
            removeEdgesTo(getExitNode());
            for (CFGEdge edge : otherCFG.ingoingEdges(otherCFG.getExitNode()))
                addEdge(edge.getSource(), getExitNode(), edge.getLabel());
        }
    }

    // 处理if-else，while等情况
    public void mountCFG(CFGNode branchNode, CFGNode mergeNode, CFG cfg, String label) {
        // 在if-else中，cfg为elseBlock对应的CFG，branchNode为condition对应的CFGNode，mergeNode为该Block对应的ExitNode
        // 在while语句中，cfg为whileBody对应的CFG，branchNode和mergeNode为Condition对应的CFGNode
        // 在for语句中,cfg为forBody，branchNode为forCondition，mergeNode为forExpression或者forCondition
        if (!cfg.isEmpty()) {
            addCFG(cfg);
            for (CFGEdge edge : cfg.outgoingEdges(cfg.getEntryNode()))
                addEdge(branchNode, edge.getDestination(), label);
            for (CFGEdge edge : cfg.ingoingEdges(cfg.getExitNode()))
                addEdge(edge.getSource(), mergeNode, edge.getLabel());
        }
        else
            addEdge(branchNode, mergeNode, label);
    }

    private void addVertices(CFG cfg) {
        // 将cfg所有的非Entry和Exit的结点添加到当前CFG
        for (CFGNode vertex : cfg.getVertices())
            // do not add entry and exit node
            if (!(vertex.equals(cfg.getEntryNode()) || vertex.equals(cfg.getExitNode())))
                addVertex(vertex);
    }

    private void addEdges(CFG cfg) {
        // 将cfg所有非Entry和Exit的边添加到当前CFG
        for (CFGNode vertex : cfg.getVertices())
            for (CFGEdge edge : cfg.outgoingEdges(vertex))
                if (!(edge.getSource().equals(cfg.getEntryNode()) || edge.getDestination().equals(cfg.getExitNode())))
                    addEdge(edge);
    }

    public void addEdge(CFGNode srcBlock, CFGNode dstBlock) {
        addEdge(srcBlock, dstBlock, CFGEdge.EMPTY_LABEL);
    }

    public void addEdge(CFGNode srcBlock, CFGNode dstBlock, String label) {
        CFGEdge edge = new CFGEdge(srcBlock, dstBlock, label);
        addEdge(edge);
    }
}
