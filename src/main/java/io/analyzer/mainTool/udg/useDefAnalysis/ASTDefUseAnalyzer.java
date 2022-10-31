package io.analyzer.mainTool.udg.useDefAnalysis;

import io.analyzer.mainTool.udg.useDefAnalysis.environments.*;
import io.analyzer.mainTool.udg.useDefGraph.UseOrDef;
import io.analyzer.mainTool.udg.ASTProvider;

import java.util.*;

public class ASTDefUseAnalyzer {
    Stack<UseDefEnvironment> environmentStack = new Stack<>();
    HashSet<UseOrDef> useDefsOfBlock = new HashSet<>();
    CalleeInfos calleeInfos = new CalleeInfos();

    private void reset() {
        environmentStack.clear();
        useDefsOfBlock.clear();
    }

    public void setCalleeInfos(CalleeInfos calleeInfos) {
        this.calleeInfos = calleeInfos;
    }

    /**
     * Analyze an AST to determine the symbols used and defined by each AST
     * node.
     *
     * */

    public Collection<UseOrDef> analyzeAST(ASTProvider astProvider) {
        reset();
        traverseAST(astProvider);
        return useDefsOfBlock;
    }


    private void traverseAST(ASTProvider astProvider) {
        UseDefEnvironment env = createUseDefEnvironment(astProvider);
        env.setASTProvider(astProvider);
        traverseASTChildren(astProvider, env);
    }

    private void traverseASTChildren(ASTProvider astProvider, UseDefEnvironment env) {
        int numChildren = astProvider.getChildCount();

        environmentStack.push(env);
        for (int i = 0; i < numChildren; i++) {
            ASTProvider childProvider = astProvider.getChild(i);
            traverseAST(childProvider);

            Collection<UseOrDef> toEmit = env.useOrDefsFromSymbols(childProvider);
            emitUseOrDefs(toEmit);
        }
        environmentStack.pop();
        reportUpstream(env);
    }

    /**
     * Creates a UseDefEnvironment for a given AST node.
     * */

    private UseDefEnvironment createUseDefEnvironment(ASTProvider astProvider) {
        String nodeType = astProvider.getTypeAsString();
        switch (nodeType) {
            case "AssignmentExpr":
                return new AssignmentEnvironment();
            case "IncDecOp":
                return new IncDecEnvironment();
            case "IdentifierDecl":
            case "Parameter":
                return new DeclEnvironment();

            case "CallExpression":
                return createCallEnvironment();

            case "Argument":
                return createArgumentEnvironment(astProvider);

            case "PtrMemberAccess":
                return new PtrMemberAccessEnvironment();

            case "MemberAccess":
                return new MemberAccessEnvironment();
            // condition和return中只有use没有def
            case "Condition":
            case "ReturnStatement":
                return new UseEnvironment();

            case "ArrayIndexing":
                return new ArrayIndexingEnvironment();

            case "UnaryOp":
                return new UnaryOpEnvironment();

            case "Identifier":
                return new IdentifierEnvironment();

            default:
                return new UseDefEnvironment();
        }
    }


    private UseDefEnvironment createCallEnvironment() {
        return new CallEnvironment();
    }

    private ArgumentEnvironment createArgumentEnvironment(ASTProvider astProvider) {
        ArgumentEnvironment argEnv = new ArgumentEnvironment();
        CallEnvironment callEnv = (CallEnvironment) environmentStack.get(environmentStack.size() - 2);
        // 该参数是否使用指针
        if (calleeInfos.judgeUse(callEnv, astProvider.getChildNumber()))
            argEnv.setIsUsePointer();
        if (calleeInfos.judgeDef(callEnv, astProvider.getChildNumber()))
            argEnv.setIsDefPointer();
        return argEnv;
    }

    /**
     * Gets upstream symbols from environment and passes them to
     * parent-environment by calling addChildSymbols on the parent. Asks
     * parent-environment to generate useOrDefs and emit them.
     * */

    private void reportUpstream(UseDefEnvironment env) {

        LinkedList<String> symbols = env.upstreamSymbols();
        ASTProvider astProvider = env.getASTProvider();
        try {
            UseDefEnvironment parentEnv = environmentStack.peek();
            parentEnv.addChildSymbols(symbols, astProvider);
        }
        catch (EmptyStackException ex) {
            // stack is empty, we've reached the root.
            // Nothing to do.
        }
    }

    private void emitUseOrDefs(Collection<UseOrDef> toEmit) {
        for (UseOrDef useOrDef : toEmit)
            useDefsOfBlock.add(useOrDef);
    }
}
