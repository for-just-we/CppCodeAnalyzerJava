package io.analyzer.mainTool.cpg;

import io.analyzer.mainTool.ast.ASTNode;
import com.alibaba.fastjson.JSON;
import com.ibm.icu.impl.Pair;


import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;

public class ToJsonUtil {
    // 将一个AST子树中的内容json化
    public static Map<String, Object> transformASTNode(ASTNode astNode) {
        List<List<String>> contents = new ArrayList<>();
        List<List<Integer>> astEdges = new ArrayList<>();
        // 这里只引用了顶层结点的行号，需要其它的可以自行添加
        int line = astNode.getLocation().startLine;
        int idx = 0;

        Queue<Pair<Integer, ASTNode>> queue = new LinkedTransferQueue<>();
        queue.add(Pair.of(-1, astNode));
        while (!queue.isEmpty()) {
            Pair<Integer, ASTNode> cur = queue.poll();
            ASTNode node = cur.second;
            contents.add(List.of(node.getTypeAsString(), node.getEscapedCodeStr(), node.getOperatorCode()));
            int parentIdx = cur.first;

            if (parentIdx != -1)
                astEdges.add(List.of(parentIdx, idx));
            for (int i = 0; i < node.getChildCount(); ++i) {
                ASTNode childNode = node.getChild(i);
                queue.add(Pair.of(idx, childNode));
            }
            idx += 1;
        }
        return Map.of("contents", contents, "edges", astEdges, "line", line);
    }

    //
    public static Map<String, Object> prettyJsonAST(Map<String, Object> jsonAST){
        List<List<String>> rawContents = (List<List<String>>) jsonAST.get("contents");
        List<String> contents = rawContents.stream().map(JSON::toJSONString).collect(Collectors.toList());
        List<List<Integer>> astEdges = (List<List<Integer>>) jsonAST.get("edges");
        return Map.of("contents", contents, "edges", astEdges,
                "line", jsonAST.get("line"));
    }

    // 将1个CPG中的内容json化
    public static Map<String, Object> transformCPG(CPG cpg){
        List<Map<String, Object>> jsonStatements = cpg.getStatements().stream()
                .map(ToJsonUtil::transformASTNode).collect(Collectors.toList());
        List<List<Object>> cfgEdges = cpg.getCfgEdges().stream().
                map(CodeEdge::toJson).collect(Collectors.toList());
        List<List<Object>> cdgEdges = cpg.getCdgEdges().stream().
                map(CodeEdge::toJson).collect(Collectors.toList());
        List<List<Object>> ddgEdges = cpg.getDdgEdges().stream().
                map(CodeEdge::toJson).collect(Collectors.toList());

        return Map.of("fileName", cpg.fileName, "functionName", cpg.funcName, "nodes", jsonStatements,
                "cfgEdges", cfgEdges, "cdgEdges", cdgEdges, "ddgEdges", ddgEdges);
    }

    // 美化输出
    public static Map<String, Object> prettyJsonCPG(Map<String, Object> jsonCPG){
        List<Map<String, Object>> jsonStatements = (List<Map<String, Object>>) jsonCPG.get("nodes");
        List<String> prettyJsonStatements = jsonStatements.stream().map(JSON::toJSONString).collect(Collectors.toList());

        List<List<Object>> jsonCfgEdges = (List<List<Object>>) jsonCPG.get("cfgEdges");
        List<String> prettyJsonCfgEdges = jsonCfgEdges.stream().map(JSON::toJSONString).
                collect(Collectors.toList());

        List<List<Object>> jsonCdgEdges = (List<List<Object>>) jsonCPG.get("cdgEdges");
        List<String> prettyJsonCdgEdges = jsonCdgEdges.stream().map(JSON::toJSONString).
                collect(Collectors.toList());

        List<List<Object>> jsonDdgEdges = (List<List<Object>>) jsonCPG.get("ddgEdges");
        List<String> prettyJsonDdgEdges = jsonDdgEdges.stream().map(JSON::toJSONString).
                collect(Collectors.toList());

        return Map.of("fileName", jsonCPG.get("fileName"), "functionName", jsonCPG.get("functionName"),
                "nodes", prettyJsonStatements, "cfgEdges", prettyJsonCfgEdges,
                "cdgEdges", prettyJsonCdgEdges, "ddgEdges", prettyJsonDdgEdges);
    }

    // 美化输出
    public static Map<String, Object> prettyJsonXFG(Map<String, Object> jsonXFG){
        List<Map<String, Object>> jsonStatements = (List<Map<String, Object>>) jsonXFG.get("line-nodes");
        List<String> prettyJsonStatements = jsonStatements.stream().map(JSON::toJSONString).collect(Collectors.toList());

        List<List<Object>> jsonCdgEdges = (List<List<Object>>) jsonXFG.get("control-dependences");
        List<String> prettyJsonCdgEdges = jsonCdgEdges.stream().map(JSON::toJSONString).
                collect(Collectors.toList());

        List<List<Object>> jsonDdgEdges = (List<List<Object>>) jsonXFG.get("data-dependences");
        List<String> prettyJsonDdgEdges = jsonDdgEdges.stream().map(JSON::toJSONString).
                collect(Collectors.toList());

        List<List<Object>> jsonLineNos = (List<List<Object>>) jsonXFG.get("line-Nos");
        List<String> prettyJsonLineNos = jsonLineNos.stream().map(JSON::toJSONString).
                collect(Collectors.toList());

        return Map.of(
                "keyline", jsonXFG.get("keyline"),
                "id2file", jsonXFG.get("id2file"),
                "line-Nos", prettyJsonLineNos,
                "line-contents", jsonXFG.get("line-contents"),
                "line-nodes", prettyJsonStatements,
                "control-dependences", prettyJsonCdgEdges,
                "data-dependences", prettyJsonDdgEdges
        );
    }
}
