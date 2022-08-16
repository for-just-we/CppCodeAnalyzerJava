package extraTools.vuldetect.utils;

import com.ibm.icu.impl.Pair;
import mainTool.ast.ASTNode;
import mainTool.cpg.CPG;
import mainTool.udg.ASTNodeASTProvider;

import java.util.*;

public class SymbolizingTool {
    private Set<String> systemDefinedVars;
    private Set<String> systemDefinedFuncs;
    private Map<String, String> var2symbol = new HashMap<>();
    private Map<String, String> func2symbol = new HashMap<>();

    private final String symbolic_func_prefix = "FUNC";
    private final String symbolic_var_prefix = "VAR";

    public void clear(){
        var2symbol.clear();
        func2symbol.clear();
    }

    public SymbolizingTool(Set<String> systemDefinedVars, Set<String> systemDefinedFuncs) {
        this.systemDefinedVars = systemDefinedVars;
        this.systemDefinedFuncs = systemDefinedFuncs;
    }

    public Pair<Set<String>, Set<String>> getVarFuncNamesInFunc(List<ASTNode> statements){
        ASTVarAnalyzer astVarAnalyzer = new ASTVarAnalyzer();
        Set<String> varSets = new HashSet<>();
        Set<String> funcSets = new HashSet<>();
        for (ASTNode statement: statements){
            ASTNodeASTProvider provider = new ASTNodeASTProvider();
            provider.setNode(statement);
            astVarAnalyzer.analyzeAST(provider);
            varSets.addAll(astVarAnalyzer.variables);
            funcSets.addAll(astVarAnalyzer.funcNames);
        }
        return Pair.of(varSets, funcSets);
    }

    // cpgs is all cpgs from functions of a program
    public void getVarFuncNamesInFile(List<CPG> cpgs){
        for (CPG cpg: cpgs){
            if (!systemDefinedFuncs.contains(cpg.funcName) && !func2symbol.containsKey(cpg.funcName))
                func2symbol.put(cpg.funcName, symbolic_func_prefix + (var2symbol.size() + 1));
            Pair<Set<String>, Set<String>> sets = getVarFuncNamesInFunc(cpg.getStatements());
            for (String var: sets.first)
                if (!var2symbol.containsKey(var) && !systemDefinedVars.contains(var))
                    var2symbol.put(var, symbolic_var_prefix + (var2symbol.size() + 1));
            for (String func: sets.second)
                if (!func2symbol.containsKey(func) && !systemDefinedFuncs.contains(func))
                    func2symbol.put(func, symbolic_func_prefix + (func2symbol.size() + 1));
        }
    }

    public String symbolize(String code){
        List<String> tokens = List.of(code.split(" "));
        List<String> symbolized_tokens = new ArrayList<>();

        for (String token: tokens){
            String symVarName = var2symbol.getOrDefault(token, null);
            String symFuncName = func2symbol.getOrDefault(token, null);
            if (symVarName != null)
                symbolized_tokens.add(symVarName);
            else if (symFuncName != null)
                symbolized_tokens.add(symFuncName);
            else
                symbolized_tokens.add(token);
        }

        return String.join(" ", symbolized_tokens);
    }
}
