package io.analyzer.extraTools.utils;

import io.analyzer.mainTool.udg.ASTProvider;
import io.analyzer.extraTools.utils.environments.*;

import java.util.*;

public class ASTVarAnalyzer {
    private Stack<VariableEnvironment> environmentStack = new Stack<>();
    public Set<String> variables;
    public Map<String, String> var2type = new HashMap();
    public Set<String> funcNames = new HashSet<>();


    public void reset(){
        environmentStack.clear();
        variables = new HashSet<>();
        funcNames = new HashSet<>();
    }

    public void analyzeAST(ASTProvider astProvider){
        reset();
        traverseAST(astProvider);
    }

    public void traverseAST(ASTProvider astProvider){
        VariableEnvironment env = createVarEnvironment(astProvider);
        traverseASTChildren(astProvider, env);
    }

    public void traverseASTChildren(ASTProvider astProvider, VariableEnvironment env){
        int numChildren = astProvider.getChildCount();
        environmentStack.push(env);
        for (int i = 0; i < numChildren; ++i){
            ASTProvider childProvider = astProvider.getChild(i);
            traverseAST(childProvider);
        }
        environmentStack.pop();
        variables.addAll(env.selfHandledSymbols());
        reportUpstream(env);
        var2type.putAll(env.var2type);
        funcNames.addAll(env.funcNames);
    }

    public void reportUpstream(VariableEnvironment env){
        Set<String> symbols = env.upstreamSymbols();
        ASTProvider astProvider = env.astProvider;
        if (!environmentStack.isEmpty()) {
            VariableEnvironment parentEnv = environmentStack.peek();
            parentEnv.addChildSymbols(symbols, astProvider);
        }
    }

    public VariableEnvironment createVarEnvironment(ASTProvider astProvider){
        String nodeType = astProvider.getTypeAsString();

        switch (nodeType){
            case "IdentifierDecl":
            case "Parameter":
                return new VarDeclEnvironment(astProvider);
            case "CallExpression":
                return new CallVarEnvironment(astProvider);
            case "ClassStaticIdentifier":
                return new ClassStaticIdentifierVarEnvironment(astProvider);
            case "Identifier":
                return new IdentifierVarEnvironment(astProvider);
            case "MemberAccess":
            case "PtrMemberAccess":
                return new MemberAccessVarEnvironment(astProvider);
            case "Callee":
                return new CalleeEnvironment(astProvider);
            default:
                return new VariableEnvironment(astProvider);
        }
    }
}
