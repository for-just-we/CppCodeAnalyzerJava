package mainTool.cfg.CPP;

import java.util.Map.Entry;

import mainTool.ast.ASTNode;
import mainTool.ast.functionDef.FunctionDef;
import mainTool.ast.functionDef.Parameter;
import mainTool.ast.functionDef.ParameterList;
import mainTool.ast.statements.blockstarters.*;
import mainTool.ast.statements.jump.BreakStatement;
import mainTool.ast.statements.CompoundStatement;
import mainTool.ast.statements.jump.ContinueStatement;
import mainTool.ast.statements.jump.GotoStatement;
import mainTool.ast.statements.Label;
import mainTool.ast.statements.jump.ReturnStatement;
import mainTool.cfg.CFG;
import mainTool.cfg.CFGEdge;
import mainTool.cfg.CFGFactory;
import mainTool.cfg.nodes.ASTNodeContainer;
import mainTool.cfg.nodes.CFGErrorNode;
import mainTool.cfg.nodes.CFGNode;
import mainTool.cfg.nodes.InfiniteForNode;

public class CCFGFactory extends CFGFactory {
    private static final StructuredFlowVisitor structuredFlowVisitior = new StructuredFlowVisitor();

    @Override
    public CFG newInstance(FunctionDef functionDefinition)
    {
        try {
            // 函数返回CFG
            CCFG function = newInstance();
            function.setName(functionDefinition.name.getEscapedCodeStr());
            // 求解parameterBlock对应的cfg
            CCFG parameterBlock = convert(functionDefinition.getParameterList());
            // 求解functionBody对应的cfg
            CCFG functionBody = convert(functionDefinition.getContent());
            parameterBlock.appendCFG(functionBody);
            function.appendCFG(parameterBlock);

            // 语句中还存在goto和return语句
            fixGotoStatements(function);
            fixReturnStatements(function);
            if (!function.getBreakStatements().isEmpty()) {
                System.err.println("warning: unresolved break statement");
                fixBreakStatements(function, function.getErrorNode()); // 处理break语句
            }
            if (!function.getContinueStatements().isEmpty()) {
                System.err.println("warning: unresolved continue statement");
                fixContinueStatement(function, function.getErrorNode()); // 处理continue语句
            }
            return function;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();  // 返回错误结点
        }
    }


    // nodes为一个Block下所有AST子结点，nodes为空时返回一个只有Entry和Exit结点的CFG
    public static CCFG newInstance(ASTNode... nodes)
    {
        try {
            CCFG block = new CCFG();
            CFGNode last = block.getEntryNode();
            for (ASTNode node : nodes) {
                CFGNode container = new ASTNodeContainer(node);
                block.addVertex(container);
                block.addEdge(last, container);
                last = container;
            }
            block.addEdge(last, block.getExitNode());
            return block;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    public static CCFG newErrorInstance() {
        CCFG errorBlock = new CCFG();
        CFGNode errorNode = new CFGErrorNode();
        errorBlock.addVertex(errorNode);
        errorBlock.addEdge(errorBlock.getEntryNode(), errorNode);
        errorBlock.addEdge(errorNode, errorBlock.getExitNode());
        return errorBlock;
    }

    // 求解IfStatement对应的CFG
    public static CFG newInstance(IfStatement ifStatement) {
        try {
            CCFG block = new CCFG();

            // 先处理condition
            CFGNode conditionContainer = new ASTNodeContainer(ifStatement.getCondition());
            block.addVertex(conditionContainer);
            block.addEdge(block.getEntryNode(), conditionContainer);

            // 再处理block
            CFG ifBlock = convert(ifStatement.getStatement());
            block.mountCFG(conditionContainer, block.getExitNode(), ifBlock, CFGEdge.TRUE_LABEL);

            // 如果还有else那就处理else
            if (ifStatement.getElseNode() != null) {
                CFG elseBlock = convert(ifStatement.getElseNode().getStatement());
                block.mountCFG(conditionContainer, block.getExitNode(), elseBlock, CFGEdge.FALSE_LABEL);
            }
            else
                block.addEdge(conditionContainer, block.getExitNode(), CFGEdge.FALSE_LABEL);
            return block;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    // 处理while语句
    public static CFG newInstance(WhileStatement whileStatement) {
        try {
            CCFG whileBlock = new CCFG();
            // 首先处理while condition
            CFGNode conditionContainer = new ASTNodeContainer(whileStatement.getCondition());
            whileBlock.addVertex(conditionContainer);
            whileBlock.addEdge(whileBlock.getEntryNode(), conditionContainer);

            // while对应的循环主体
            CFG whileBody = convert(whileStatement.getStatement());

            whileBlock.mountCFG(conditionContainer, conditionContainer, whileBody, CFGEdge.TRUE_LABEL);
            whileBlock.addEdge(conditionContainer, whileBlock.getExitNode(),  CFGEdge.FALSE_LABEL);

            // 考虑break连接到exit
            fixBreakStatements(whileBlock, whileBlock.getExitNode());
            // continue连接到condition
            fixContinueStatement(whileBlock, conditionContainer);

            return whileBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    // 处理for循环，与while相比，for的CFG会先执行forInit，然后condition和（ForBody + expression）组成循环
    public static CFG newInstance(ForStatement forStatement) {
        try {
            CCFG forBlock = new CCFG();
            // 处理forInit，forCondition和表达式
            ASTNode initialization = forStatement.getForInitStatement();
            ASTNode condition = forStatement.getCondition();
            ASTNode expression = forStatement.getExpression();

            // forBody
            CFG forBody = convert(forStatement.getStatement());
            CFGNode conditionContainer;

            if (condition != null)
                conditionContainer = new ASTNodeContainer(condition);
            else //没条件的话该for循环不会终止
                conditionContainer = new InfiniteForNode();


            forBlock.addVertex(conditionContainer);
            // condition连接到ExitNode的false边
            forBlock.addEdge(conditionContainer, forBlock.getExitNode(),CFGEdge.FALSE_LABEL);

            if (initialization != null) {
                // initialization不为null的话，将initializationContainer插入到Entry和condition之间
                CFGNode initializationContainer = new ASTNodeContainer(initialization);
                forBlock.addVertex(initializationContainer);
                forBlock.addEdge(forBlock.getEntryNode(), initializationContainer);
                forBlock.addEdge(initializationContainer, conditionContainer);
            }
            else
                // 否则Entry直接连接到condition
                forBlock.addEdge(forBlock.getEntryNode(), conditionContainer);


            if (expression != null) {
                // expression不为null的话
                CFGNode expressionContainer = new ASTNodeContainer(expression);
                forBlock.addVertex(expressionContainer);
                forBlock.addEdge(expressionContainer, conditionContainer);
                forBlock.mountCFG(conditionContainer, expressionContainer, forBody, CFGEdge.TRUE_LABEL);
            }
            else
                forBlock.mountCFG(conditionContainer, conditionContainer, forBody, CFGEdge.TRUE_LABEL);

            // break直接连接到exit
            fixBreakStatements(forBlock, forBlock.getExitNode());
            // continue连接到condition
            fixContinueStatement(forBlock, conditionContainer);

            return forBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }


    // do-while语句
    public static CFG newInstance(DoStatement doStatement) {
        try {
            CCFG doBlock = new CCFG();
            // do-while condition
            CFGNode conditionContainer = new ASTNodeContainer(doStatement.getCondition());

            doBlock.addVertex(conditionContainer);
            doBlock.addEdge(conditionContainer, doBlock.getExitNode(), CFGEdge.FALSE_LABEL);
            // do-while body
            CFG doBody = convert(doStatement.getStatement());

            doBlock.mountCFG(doBlock.getEntryNode(), conditionContainer, doBody, CFGEdge.EMPTY_LABEL);

            for (CFGEdge edge : doBody.outgoingEdges(doBody.getEntryNode()))
                doBlock.addEdge(conditionContainer, edge.getDestination(), CFGEdge.TRUE_LABEL);
            // 该层循环中的所有break直接连接到循环exit
            fixBreakStatements(doBlock, doBlock.getExitNode());
            // continue直接连接到condition
            fixContinueStatement(doBlock, conditionContainer);

            return doBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    // switch语句
    public static CFG newInstance(SwitchStatement switchStatement) {
        try {
            CCFG switchBlock = new CCFG();
            // 处理condition
            CFGNode conditionContainer = new ASTNodeContainer(switchStatement.getCondition());
            switchBlock.addVertex(conditionContainer);
            switchBlock.addEdge(switchBlock.getEntryNode(), conditionContainer);

            CCFG switchBody = convert(switchStatement.getStatement());
            switchBlock.addCFG(switchBody);

            boolean defaultLabel = false;
            // 获取每个case: xxx
            for (Entry<String, CFGNode> block : switchBody.getLabels().entrySet()) {
                // 如果内容是default
                if (block.getKey().equals("default"))
                    defaultLabel = true;
                switchBlock.addEdge(conditionContainer, block.getValue(), block.getKey());
            }
            for (CFGEdge edge : switchBody.ingoingEdges(switchBody.getExitNode()))
                switchBlock.addEdge(edge.getSource(), switchBlock.getExitNode());
            // 不存在defalut标签的话，添加一条condition到switch结尾的CFG边
            if (!defaultLabel)
                switchBlock.addEdge(conditionContainer, switchBlock.getExitNode());

            // switch中break直接与switch end相连
            fixBreakStatements(switchBlock, switchBlock.getExitNode());
            return switchBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    // for range语句
    public static CFG newInstance(ForRangeStatement forRangeStatement){
        try {
            CCFG forBlock = new CCFG();
            // 处理forRangeInit，forCondition和表达式
            ASTNode forRangeInit = forRangeStatement.getForRangeInit();
            // forBody
            CFG forBody = convert(forRangeStatement.getStatement());
            CFGNode conditionContainer = new InfiniteForNode();
            forBlock.addVertex(conditionContainer);

            // Entry到InfiniteFor
            forBlock.addEdge(forBlock.getEntryNode(), conditionContainer);
            // condition连接到ExitNode的false边
            forBlock.addEdge(conditionContainer, forBlock.getExitNode(), CFGEdge.FALSE_LABEL);
            CFGNode initializationContainer = new ASTNodeContainer(forRangeInit);
            forBlock.addVertex(initializationContainer);
            // condition -> for range init
            forBlock.addEdge(conditionContainer, initializationContainer, CFGEdge.TRUE_LABEL);
            // init -> forBody -> condition
            forBlock.mountCFG(initializationContainer, conditionContainer, forBody, CFGEdge.EMPTY_LABEL);

            // break直接连接到exit
            fixBreakStatements(forBlock, forBlock.getExitNode());
            // continue连接到condition
            fixContinueStatement(forBlock, conditionContainer);

            return forBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    // Try-Catch
    public static CFG newInstance(TryStatement tryStatement){
        return newInstance(tryStatement.getContent());
    }

    // 其它语句
    public static CFG newInstance(ParameterList paramList) {
        try {
            CFG parameterListBlock = newInstance();
            for (Parameter parameter : paramList.getParameters())
                parameterListBlock.appendCFG(convert(parameter));

            return parameterListBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    public static CFG newInstance(CompoundStatement content) {
        try {
            CFG compoundBlock = newInstance();
            for (ASTNode statement : content.getStatements())
                compoundBlock.appendCFG(convert(statement));
            return compoundBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    // return语句
    public static CFG newInstance(ReturnStatement returnStatement) {
        try {
            CCFG returnBlock = new CCFG();
            CFGNode returnContainer = new ASTNodeContainer(returnStatement);
            returnBlock.addVertex(returnContainer);
            returnBlock.addEdge(returnBlock.getEntryNode(), returnContainer);
            returnBlock.addEdge(returnContainer, returnBlock.getExitNode());
            returnBlock.addReturnStatement(returnContainer);
            return returnBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    // goto语句
    public static CFG newInstance(GotoStatement gotoStatement) {
        try {
            CCFG gotoBlock = new CCFG();
            CFGNode gotoContainer = new ASTNodeContainer(gotoStatement);
            gotoBlock.addVertex(gotoContainer);
            gotoBlock.addEdge(gotoBlock.getEntryNode(), gotoContainer);
            gotoBlock.addEdge(gotoContainer, gotoBlock.getExitNode());
            gotoBlock.addGotoStatement(gotoContainer, gotoStatement.getTarget());
            return gotoBlock;
        }
        catch (Exception e)
        {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    public static CFG newInstance(Label labelStatement) {
        try {
            CCFG continueBlock = new CCFG();
            CFGNode labelContainer = new ASTNodeContainer(labelStatement);
            continueBlock.addVertex(labelContainer);
            continueBlock.addEdge(continueBlock.getEntryNode(), labelContainer);
            continueBlock.addEdge(labelContainer, continueBlock.getExitNode());
            String label = labelStatement.getEscapedCodeStr();
            label = label.substring(0, label.length() - 2);
            continueBlock.addBlockLabel(label, labelContainer);
            return continueBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    public static CFG newInstance(ContinueStatement continueStatement)
    {
        try
        {
            CCFG continueBlock = new CCFG();
            CFGNode continueContainer = new ASTNodeContainer(continueStatement);
            continueBlock.addVertex(continueContainer);
            continueBlock.addEdge(continueBlock.getEntryNode(),
                    continueContainer);
            continueBlock.addEdge(continueContainer,
                    continueBlock.getExitNode());
            continueBlock.addContinueStatement(continueContainer);
            return continueBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }

    public static CFG newInstance(BreakStatement breakStatement) {
        try {
            CCFG breakBlock = new CCFG();
            CFGNode breakContainer = new ASTNodeContainer(breakStatement);
            breakBlock.addVertex(breakContainer);
            breakBlock.addEdge(breakBlock.getEntryNode(), breakContainer);
            breakBlock.addEdge(breakContainer, breakBlock.getExitNode());
            breakBlock.addBreakStatement(breakContainer);
            return breakBlock;
        }
        catch (Exception e) {
            // e.printStackTrace();
            return newErrorInstance();
        }
    }


    // jump类语句处理，添加CFG边
    public static void fixBreakStatements(CCFG thisCFG, CFGNode target) {
        for (CFGNode breakStatement : thisCFG.getBreakStatements()) {
            // fix之前break语句与后面的CFG结点有CFG边相连，现在全部删除
            thisCFG.removeEdgesFrom(breakStatement);
            // break语句无条件跳转到target
            thisCFG.addEdge(breakStatement, target);
        }
        thisCFG.getBreakStatements().clear();
    }

    // continue语句
    public static void fixContinueStatement(CCFG thisCFG, CFGNode target)
    {
        for (CFGNode continueStatement : thisCFG.getContinueStatements()) {
            // 删除与continue语句直接相连的边
            thisCFG.removeEdgesFrom(continueStatement);
            // target为continue跳转到的结点
            thisCFG.addEdge(continueStatement, target);
        }
        thisCFG.getContinueStatements().clear();
    }

    public static void fixGotoStatements(CCFG thisCFG) {
        for (Entry<CFGNode, String> entry : thisCFG.getGotoStatements().entrySet()) {
            CFGNode gotoStatement = entry.getKey();
            String label = entry.getValue();
            // 删除与goto语句相连的CFG边
            thisCFG.removeEdgesFrom(gotoStatement);
            // 添加CFG边到block结点跳转的边
            thisCFG.addEdge(gotoStatement, thisCFG.getBlockByLabel(label));
        }
        thisCFG.getGotoStatements().clear();
    }

    public static void fixReturnStatements(CCFG thisCFG)
    {
        for (CFGNode returnStatement : thisCFG.getReturnStatements()) {
            // 删除return相连的边
            thisCFG.removeEdgesFrom(returnStatement);
            // return连接到exit
            thisCFG.addEdge(returnStatement, thisCFG.getExitNode());
        }
        thisCFG.getReturnStatements().clear();
    }


    // 递归遍历AST生成CFG
    public static CCFG convert(ASTNode node) {
        CCFG cfg;
        if (node != null) {
            node.accept(structuredFlowVisitior);
            cfg = (CCFG) structuredFlowVisitior.getCFG();
        }
        else
            cfg = newInstance();
        return cfg;
    }
}
