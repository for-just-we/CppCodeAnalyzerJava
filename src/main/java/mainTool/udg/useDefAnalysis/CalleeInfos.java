package mainTool.udg.useDefAnalysis;


import mainTool.udg.useDefAnalysis.environments.CallEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 这个类主要记录函数调用中参数为指针变量的信息，等同于Joern中的TaintSource，但是Joern考虑了指针def没考虑指针use的情况
public class CalleeInfos {
    // 假设key为memset，value为[0]，表示memset的第0个参数使用了指针变量，memset(a, xx,xx); 中use的symbol要包含 * a
    Map<String, List<Integer>> calleeToArgUseIds = new HashMap<>();
    // 假设key为gets, value为[0]，表示gets函数重新定义了第0个指针参数，gets(buf) 重新定义了symbol * buf
    Map<String, List<Integer>> calleeToArgDefIds = new HashMap<>();

    // 参数为可变参数的情况
    // 比如 scanf -> 1， 表示 scanf会重新定义第1个以后的所有参数, scanf("%d", &a) 会重新定义 a
    Map<String, Integer> calleeToDefStartIds = new HashMap<>();

    // 判断是否使用指针
    public boolean judgeUse(CallEnvironment callEnv, int childNumber){
        // getASTProvider对应CallExpression，getChild(0)对应Callee
        String callee = callEnv.getASTProvider().getChild(0).getEscapedCodeStr();
        return calleeToArgUseIds.containsKey(callee) && calleeToArgUseIds.get(callee).contains(childNumber);
    }

    // 判断是否定义指针
    public boolean judgeDef(CallEnvironment callEnv, int childNumber){
        String callee = callEnv.getASTProvider().getChild(0).getEscapedCodeStr();
        if (calleeToArgDefIds.containsKey(callee) && calleeToArgDefIds.get(callee).contains(childNumber))
            return true;
        else
            return calleeToDefStartIds.containsKey(callee) && childNumber >= calleeToDefStartIds.get(callee);
    }

    public void addArgUse(String callee, int argN){
        if (!calleeToArgUseIds.containsKey(callee))
            calleeToArgUseIds.put(callee, new ArrayList<>());
        calleeToArgUseIds.get(callee).add(argN);
    }

    public void addArgDef(String callee, int argN){
        if (!calleeToArgDefIds.containsKey(callee))
            calleeToArgDefIds.put(callee, new ArrayList<>());
        calleeToArgDefIds.get(callee).add(argN);
    }

    public void addArgDefStartIds(String callee, int argN){
        calleeToDefStartIds.put(callee, argN);
    }
}
