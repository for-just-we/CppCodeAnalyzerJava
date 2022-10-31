package io.analyzer.mainTool.cpg;

import io.analyzer.mainTool.graphutils.Edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CodeEdge extends Edge<Integer> {
    private String property;

    public CodeEdge(Integer source, Integer destination, String property) {
        super(source, destination);
        this.property = property;
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    @Override
    public String toString() {
        if (property == null)
            return String.format("%d ---- %d", this.getSource(), this.getDestination());
        return String.format("%d --[%s]-- %d", this.getSource(), this.property, this.getDestination());
    }

    public List<Object> toJson(){
        List<Object> datas = new ArrayList(Arrays.asList(this.getSource(), this.getDestination()));
        if (property != null)
            datas.add(property);
        return datas;
    }

    public static CodeEdge fromJson(List<Object> datas){
        assert datas.size() == 2 || datas.size() == 3;
        if (datas.size() == 3)
            return new CodeEdge((Integer) datas.get(0), (Integer) datas.get(1), (String) datas.get(2));
        return new CodeEdge((Integer) datas.get(0), (Integer) datas.get(1), null);
    }
}
