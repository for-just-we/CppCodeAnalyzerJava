package io.analyzer.extraTools.deepwukong;

import com.alibaba.fastjson.JSON;
import com.ibm.icu.impl.Pair;
import io.analyzer.mainTool.ast.ASTNode;
import io.analyzer.mainTool.cpg.CodeEdge;
import io.analyzer.mainTool.cpg.ToJsonUtil;

import java.util.*;
import java.util.stream.Collectors;

// generating xfg defined in DeepWuKong
// following paper DeepWukong: Statically Detecting Software Vulnerabilities Using Deep Graph Neural Network
public class XFG {
    public List<CodeEdge> cdes = new ArrayList(); // control dependence edges
    public List<CodeEdge> ddes = new ArrayList(); // data dependence edges

    public Pair<Integer, Integer> keyLine; // key line 文件Id, 行号
    public List<Pair<Integer, Integer>> lineNumbers = new ArrayList<>();
    public Map<Integer, String> id2file = new HashMap<>(); // 文件id对文件名

    public List<String> contents = new ArrayList<>(); // slice中每个语句对应的token序列
    public List<ASTNode> nodes = new ArrayList<>(); // slice中每个语句对应的AST子树

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XFG)) return false;
        XFG xfg = (XFG) o;
        return contents.equals(xfg.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }

    public Map<String, Object> toJson(){
        return Map.of(
                "keyline", Arrays.asList(keyLine.first, keyLine.second),
                "id2file", JSON.toJSON(id2file),
                "line-Nos", lineNumbers.stream().map(info -> Arrays.asList(info.first, info.second)).collect(Collectors.toList()),
                "line-contents", contents,
                "line-nodes", nodes.stream().map(ToJsonUtil::transformASTNode).collect(Collectors.toList()),
                "control-dependences", cdes.stream().map(CodeEdge::toJson).collect(Collectors.toList()),
                "data-dependences", ddes.stream().map(CodeEdge::toJson).collect(Collectors.toList())
        );
    }
}
