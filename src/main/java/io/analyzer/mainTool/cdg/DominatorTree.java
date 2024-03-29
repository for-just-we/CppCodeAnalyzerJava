package io.analyzer.mainTool.cdg;

import io.analyzer.mainTool.graphutils.AbstractTwoWayGraph;
import io.analyzer.mainTool.graphutils.Edge;
import io.analyzer.mainTool.graphutils.PostorderIterator;

import java.util.*;

public class DominatorTree<V> {
    private HashMap<V, V> dominators; // key -> value 表示 key 支配 value
    private HashMap<V, Set<V>> dominanceFrontiers; // 前向支配
    private HashMap<V, Integer> postorderEnumeration; // 结点访问顺序

    private DominatorTree() {
        dominators = new HashMap<V, V>();
        dominanceFrontiers = new HashMap<V, Set<V>>();
        postorderEnumeration = new HashMap<V, Integer>();
    }

    public static <V, E extends Edge<V>> DominatorTree<V> newInstance(
            AbstractTwoWayGraph<V, E> graph, V startNode) {
        return new DominatorTreeCreator<V, E>(graph, startNode).create();
    }

    public Collection<V> getVertices() {
        return dominators.keySet();
    }

    public V getDominator(V vertex) {
        return dominators.get(vertex);
    }

    public Set<V> dominanceFrontier(V vertex) {
        return dominanceFrontiers.get(vertex);
    }

    private V commonDominator(List<V> vertices){
        Deque<V> stack = new LinkedList<V>();
        for (V vertex : vertices)
            if (hasDominator(vertex))
                stack.push(vertex);

        if (stack.isEmpty())
            return null;
        while (stack.size() > 1)
            stack.push(commonDominator(stack.pop(), stack.pop()));
        return stack.pop();
    }

    private V commonDominator(V vertex1, V vertex2) {
        V finger1 = vertex1;
        V finger2 = vertex2;
        while (!finger1.equals(finger2)) {
            while (postorderEnumeration.get(finger1) < postorderEnumeration.get(finger2))
                finger1 = getDominator(finger1);

            while (postorderEnumeration.get(finger2) < postorderEnumeration.get(finger1))
                finger2 = getDominator(finger2);
        }
        assert finger1.equals(finger2) : "fingers do not match";
        return finger1;
    }

    // 往支配树中添加结点
    private boolean addVertex(V vertex) {
        if (!contains(vertex)){
            dominators.put(vertex, null);
            return true;
        }
        return false;
    }

    //
    private boolean setDominator(V vertex, V dominator)
    {
        boolean changed = false;
        if (contains(vertex)) {
            V currentDominator = dominators.get(vertex);
            if (currentDominator == null && dominator != null) {
                dominators.put(vertex, dominator);
                changed = true;
            }
            else if (!currentDominator.equals(dominator)) {
                dominators.put(vertex, dominator);
                changed = true;
            }
            else
                changed = false;

        }
        return changed;
    }

    private boolean contains(V vertex) {
        return dominators.containsKey(vertex);
    }

    private boolean hasDominator(V vertex) {
        return dominators.get(vertex) != null;
    }

    private static class DominatorTreeCreator<V, E extends Edge<V>> {
        private DominatorTree<V> dominatorTree;
        private AbstractTwoWayGraph<V, E> graph; // 逆向CFG
        private List<V> orderedVertices; // 存储逆向CFG结点访问顺序
        private V startNode;  // 一般是逆向CFG EntryNode

        public DominatorTreeCreator(AbstractTwoWayGraph<V, E> graph, V startNode) {
            this.dominatorTree = new DominatorTree<V>();
            this.graph = graph;
            this.orderedVertices = new LinkedList<V>();
            this.startNode = startNode;
        }

        public DominatorTree<V> create() {
            // 获取逆向CFG结点访问顺序
            enumerateVertices();
            // 初始化
            initializeDominatorTree();
            // 构建支配树
            buildDominatorTree();
            // 控制依赖边计算
            determineDominanceFrontiers();
            return dominatorTree;
        }

        private void determineDominanceFrontiers() {
            for (V currentNode: orderedVertices) { // 后序遍历逆向CFG
                if (graph.inDegree(currentNode) > 1) {
                    V runner;
                    for (Edge<V> edge : graph.ingoingEdges(currentNode)) {
                        V predecessor = edge.getSource();
                        if (!orderedVertices.contains(predecessor))
                            continue;
                        // runnner序号大于currentNode
                        runner = predecessor;
                        while (!runner.equals(dominatorTree.getDominator(currentNode))) { // key支配于value
                            if (!dominatorTree.dominanceFrontiers.containsKey(runner))
                                dominatorTree.dominanceFrontiers.put(runner, new HashSet<V>());

                            dominatorTree.dominanceFrontiers.get(runner).add(currentNode);
                            runner = dominatorTree.getDominator(runner);
                        }
                    }
                }
            }
        }

        private void buildDominatorTree() {
            boolean changed = true;
            while (changed) {
                changed = false;
                // orderedVertices存储结点访问顺序
                ListIterator<V> reverseVertexIterator = orderedVertices.listIterator(orderedVertices.size());
                // 再次反向迭代
                reverseVertexIterator.previous();

                while (reverseVertexIterator.hasPrevious()) {
                    V currentNode = reverseVertexIterator.previous();
                    List<V> list = new LinkedList<V>();
                    for (Edge<V> edge : graph.ingoingEdges(currentNode))
                        list.add(edge.getSource());
                    V newIdom = dominatorTree.commonDominator(list);
                    dominatorTree.addVertex(currentNode);
                    if (dominatorTree.setDominator(currentNode, newIdom))
                        changed = true;
                }
            }
        }

        // 获取逆向CFG结点访问顺序
        private void enumerateVertices() {
            int counter = 0;
            Iterator<V> postorderIterator = new PostorderIterator<V, E>(graph, startNode);
            while (postorderIterator.hasNext()) {
                V vertex = postorderIterator.next();
                orderedVertices.add(vertex);
                dominatorTree.postorderEnumeration.put(vertex, counter++);
            }
            if (orderedVertices.size() < graph.size())
                System.out.println("warning: incomplete control flow graph");
        }

        // 初始化支配树
        private void initializeDominatorTree() {
            dominatorTree.addVertex(startNode);
            dominatorTree.setDominator(startNode, startNode);
        }

    }
}
