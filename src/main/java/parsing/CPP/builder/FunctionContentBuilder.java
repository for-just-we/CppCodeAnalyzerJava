package parsing.CPP.builder;

import antlr.Cpp.CPP14Parser;
import ast.ASTNode;
import ast.ASTNodeBuilder;
import ast.declaration.ForInit;
import ast.declaration.ForRangeInit;
import ast.declaration.IdentifierDecl;
import ast.declaration.IdentifierDeclType;
import ast.expressions.*;
import ast.expressions.binaryExpressions.*;
import ast.expressions.expressionHolders.*;
import ast.expressions.memberAccesses.MemberAccess;
import ast.expressions.memberAccesses.PtrMemberAccess;
import ast.expressions.postfixExpressions.CallExpression;
import ast.expressions.postfixExpressions.DeleteExpression;
import ast.expressions.postfixExpressions.IncDecOp;
import ast.expressions.postfixExpressions.NewExpression;
import ast.expressions.primaryExpression.*;
import ast.statements.*;
import ast.statements.blockstarters.*;
import ast.statements.jump.BreakStatement;
import ast.statements.jump.ContinueStatement;
import ast.statements.jump.GotoStatement;
import ast.statements.jump.ReturnStatement;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Stack;

public class FunctionContentBuilder extends ASTNodeBuilder {
    public Stack<ASTNode> stack = new Stack();
    public Stack<String> idType = new Stack();
    private String curType = null; // IdentifierDeclStatement, ForRangeInit, newTypeId
    private String curCompleteType = null;
    private Identifier curVarNameId = null; // 标识当前变量名

    private final String varDecl = "VarDecl";
    private final String declarator = "Declarator";

    private boolean pastTheTypeId = false; // 用来解析cast type时用，为true表示现在正在解析castTarget

    NestingReconstructor nesting = new NestingReconstructor(stack);



    @Override
    public void enterStatement(CPP14Parser.StatementContext ctx) {
        // 跳过labelstatement
        if (ctx.labeledstatement() != null)
            return;
        Statement statementItem = new Statement();
        statementItem.initializeFromContext(ctx);
        stack.push(statementItem);
    }

    @Override
    public void exitStatement(CPP14Parser.StatementContext ctx) {
        // 跳过labelstatement
        if (ctx.labeledstatement() != null)
            return;
        if (stack.size() == 0)
            throw new RuntimeException();
        ASTNode curNode = stack.pop();
        ASTNode parent = stack.peek();
        curNode.initializeFromContext(ctx);
        // 当前statement的父节点是If
        if (parent instanceof IfStatement){
            // ctx.parent就是SelectionstatementContext类型
            // 如果当前statement的父节点是If
            if (ctx == ctx.parent.getChild(4))
                parent.addChild(curNode);
            // 当前statement的父节点是Else
            else {
                ElseStatement elseStatement = new ElseStatement();
                ((IfStatement) parent).setElseNode(elseStatement);
                elseStatement.addChild(curNode);
            }
            return;
        }

        if (curNode instanceof IdentifierDeclStatement)
            curType = null;
        parent.addChild(curNode);
    }

    // 块语句
    @Override
    public void enterCompoundstatement(CPP14Parser.CompoundstatementContext ctx) {
        if (stack.peek() instanceof TryStatement || stack.peek() instanceof CatchStatement)
            stack.push(new CompoundStatement());
        else
            replaceTopOfStack(new CompoundStatement());
    }

    @Override
    public void exitCompoundstatement(CPP14Parser.CompoundstatementContext ctx) {
        // 非try-catch语句统统在exitStatement时处理
        ASTNode comp = stack.pop();
        if (!stack.empty() && (stack.peek() instanceof TryStatement || stack.peek() instanceof CatchStatement)){
            CompoundStatement compoundStatement = (CompoundStatement) comp;
            compoundStatement.initializeFromContext(ctx);
            stack.peek().addChild(compoundStatement);
        }
        else
            stack.push(comp);
    }

    // 表达式语句
    @Override
    public void enterExpressionstatement(CPP14Parser.ExpressionstatementContext ctx) {
        // expressionstatement上面可能是普通statement，也可能是forinit
        // 对于forinit，我们保留
        if (stack.peek() instanceof ForInit)
            return;
        replaceTopOfStack(new ExpressionStatement());
    }

    // if-else语句
    @Override
    public void enterIfStatement(CPP14Parser.IfStatementContext ctx) {
        replaceTopOfStack(new IfStatement());
    }

    // switch语句
    @Override
    public void enterSwitchStatement(CPP14Parser.SwitchStatementContext ctx) {
        replaceTopOfStack(new SwitchStatement());
    }

    // label
    @Override
    public void enterLabel(CPP14Parser.LabelContext ctx) {
        Label label = new Label();
        if (ctx.Case() != null)
            label.type = Label.Type.Case;
        else if (ctx.Default() != null)
            label.type = Label.Type.Default;
        else
            label.type = Label.Type.Normal;

        stack.push(label);
    }

    @Override
    public void exitLabel(CPP14Parser.LabelContext ctx) {
        Label label = (Label) stack.pop();
        label.initializeFromContext(ctx);
        stack.peek().addChild(label);
    }

    // 循环语句
    // while语句
    @Override
    public void enterWhileStatement(CPP14Parser.WhileStatementContext ctx) {
        replaceTopOfStack(new WhileStatement());
    }

    // do-while语句
    @Override
    public void enterDoStatement(CPP14Parser.DoStatementContext ctx) {
        replaceTopOfStack(new DoStatement());
    }

    // for语句
    @Override
    public void enterForStatement(CPP14Parser.ForStatementContext ctx) {
        replaceTopOfStack(new ForStatement());
    }

    // for init语句
    @Override
    public void enterForinitstatement(CPP14Parser.ForinitstatementContext ctx) {
        ForInit forinit = new ForInit();
        stack.push(forinit);
    }

    @Override
    public void exitForinitstatement(CPP14Parser.ForinitstatementContext ctx) {
        ASTNode forinit = stack.pop();
        forinit.initializeFromContext(ctx);
        ForStatement forStatement = (ForStatement) stack.peek();
        forStatement.addChild(forinit);
    }

    // forRange
    @Override
    public void enterForRangeStatement(CPP14Parser.ForRangeStatementContext ctx) {
        replaceTopOfStack(new ForRangeStatement());
    }

    // forrange decl
    @Override
    public void enterForrangedeclaration(CPP14Parser.ForrangedeclarationContext ctx) {
        ForRangeInit forRangeInit = new ForRangeInit();
        stack.push(forRangeInit);
        curType = "";
    }

    // for initializer
    @Override
    public void exitForrangeinitializer(CPP14Parser.ForrangeinitializerContext ctx) {
        ASTNode forRangeInit = stack.pop();
        forRangeInit.initializeFromContext(ctx);
        stack.peek().addChild(forRangeInit);
        curType = null;
        curCompleteType = null;
    }

    // try catch异常处理
    // try语句
    @Override
    public void enterTryblock(CPP14Parser.TryblockContext ctx) {
        replaceTopOfStack(new TryStatement());
    }

    // catch语句
    @Override
    public void enterHandler(CPP14Parser.HandlerContext ctx) {
        CatchStatement catchStatement = new CatchStatement();
        stack.push(catchStatement);
    }

    @Override
    public void exitHandler(CPP14Parser.HandlerContext ctx) {
        CatchStatement catchStatement = (CatchStatement) stack.pop();
        catchStatement.initializeFromContext(ctx);

        if (!(stack.peek() instanceof TryStatement))
            throw new RuntimeException("error when parsing try-catch block");
        TryStatement tryStatement = (TryStatement) stack.peek();
        tryStatement.getCatchList().addCatchStatement(catchStatement);
    }

    @Override
    public void enterExceptiondeclaration(CPP14Parser.ExceptiondeclarationContext ctx) {
        curType = "";
    }

    @Override
    public void exitExceptiondeclaration(CPP14Parser.ExceptiondeclarationContext ctx) {

        curType = null;
    }

    // 跳转语句
    @Override
    public void enterJumpstatement(CPP14Parser.JumpstatementContext ctx) {
        // break语句
        if (ctx.Break() != null)
            replaceTopOfStack(new BreakStatement());
        // continue语句
        else if (ctx.Continue() != null)
            replaceTopOfStack(new ContinueStatement());
        // goto语句
        else if (ctx.Goto() != null)
            replaceTopOfStack(new GotoStatement());
        // return语句
        else if (ctx.Return() != null)
            replaceTopOfStack(new ReturnStatement());
    }

    // 替换栈顶
    protected void replaceTopOfStack(ASTNode item) {
        stack.pop();
        stack.push(item);
    }

    // 变量定义IdentifierDeclStatement
    @Override
    public void enterSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx) {
        curType = "";
        if (stack.peek() instanceof ForInit)
            return;
        IdentifierDeclStatement idDeclStmt = new IdentifierDeclStatement();
        idDeclStmt.initializeFromContext(ctx);
        replaceTopOfStack(idDeclStmt);
    }

    // 处理变量定义类型curType
    @Override
    public void exitDeclspecifierseq(CPP14Parser.DeclspecifierseqContext ctx) {
        if (ctx.getChildCount() == 1)
            curType = curType.strip();
    }

    @Override
    public void enterOtherDecl(CPP14Parser.OtherDeclContext ctx) {
        idType.push(varDecl);
    }

    @Override
    public void exitOtherDecl(CPP14Parser.OtherDeclContext ctx) {
        idType.pop();
    }


    // 单个变量定义表达式
    @Override
    public void enterInitdeclarator(CPP14Parser.InitdeclaratorContext ctx) {
        curCompleteType = curType.strip();
        IdentifierDecl identifierDecl = new IdentifierDecl();
        stack.push(identifierDecl);
        IdentifierDeclType declType = new IdentifierDeclType();
        // 先设置基础类型，完整类型可能是指针类型
        declType.baseType = curType.strip();
        identifierDecl.setType(declType);
    }

    // 一个initdeclarator标识着1个变量
    @Override
    public void exitInitdeclarator(CPP14Parser.InitdeclaratorContext ctx) {
        curCompleteType = null;
        curVarNameId = null;
        ASTNode identifierDecl = stack.pop();
        identifierDecl.initializeFromContext(ctx);
        ASTNode identifierDeclStatement = stack.peek();
        identifierDeclStatement.addChild(identifierDecl);
    }

    // 处理变量名和完整类型
    @Override
    public void enterDeclarator(CPP14Parser.DeclaratorContext ctx) {
        // 标识进入变量定义
        // ForRangeInit和catch要特殊处理
        if (stack.peek() instanceof ForRangeInit || stack.peek() instanceof CatchStatement){
            curType = curType.strip();
            curCompleteType = curType;
        }
        idType.push(declarator);
    }

    @Override
    public void exitDeclarator(CPP14Parser.DeclaratorContext ctx) {
        idType.pop();
        //For Range Init
        if (stack.peek() instanceof ForRangeInit){
            ForRangeInit init = (ForRangeInit) stack.peek();
            IdentifierDeclType initType = new IdentifierDeclType();
            initType.baseType = curType;
            initType.completeType = curCompleteType;
            init.setType(initType);
            return;
        }
        else if (stack.peek() instanceof CatchStatement){
            CatchStatement catchStatement = (CatchStatement) stack.peek();
            IdentifierDeclType identifierDeclType = new IdentifierDeclType();
            identifierDeclType.baseType = curType;
            identifierDeclType.completeType = curCompleteType;
            catchStatement.setExceptionType(identifierDeclType);
            return;
        }

        // 处理当前变量的完整类型
        IdentifierDecl curDecl = (IdentifierDecl) stack.peek();
        IdentifierDeclType curVarType = curDecl.getType();
        curVarType.completeType = curCompleteType;
    }

    // 处理指针定义类型, char *p;
    @Override
    public void enterPtrDecl(CPP14Parser.PtrDeclContext ctx) {
        if (ctx.ptroperator().Star() != null)
            curCompleteType += " *";
    }

    // 处理数组定义类型，char source[100];，对于这种变量其类型为 char *
    @Override
    public void enterArrayDecl(CPP14Parser.ArrayDeclContext ctx) {
        curCompleteType += " *";
    }

    // 以下代码处理Expression
    // 条件判断Condition
    @Override
    public void enterCondition(CPP14Parser.ConditionContext ctx) {
        Condition condition = new Condition();
        stack.add(condition);
    }

    @Override
    public void exitCondition(CPP14Parser.ConditionContext ctx) {
        Condition condition = (Condition) stack.pop();
        ASTNode parentNode = stack.peek();
        parentNode.addChild(condition);
        condition.initializeFromContext(ctx);
    }

    //变量定义语句可能伴随着赋值，包括 int a = 10, b{10}, c(100);
    // 对应 int a = 10;
    @Override
    public void enterInitDeclWithAssign(CPP14Parser.InitDeclWithAssignContext ctx) {
        AssignmentExpr assignmentExpr = new AssignmentExpr();
        assignmentExpr.setOperator("=");
        assignmentExpr.flag = false; // 不再自动设置operator
        assignmentExpr.addChild(curVarNameId.copy());
        stack.push(assignmentExpr);
    }

    @Override
    public void exitInitDeclWithAssign(CPP14Parser.InitDeclWithAssignContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 列表初始化对应int a{10};
    @Override
    public void enterInitDeclWithList(CPP14Parser.InitDeclWithListContext ctx) {
        AssignmentExpr assignmentExpr = new AssignmentExpr();
        assignmentExpr.setOperator("=");
        assignmentExpr.flag = false; // 不再自动设置operator
        assignmentExpr.addChild(curVarNameId.copy());

        InitializerList initializerList = new InitializerList();
        // assignmentExpr.addChild(initializerList);
        stack.push(assignmentExpr);
        stack.push(initializerList);
    }

    @Override
    public void exitInitDeclWithList(CPP14Parser.InitDeclWithListContext ctx) {
        nesting.consolidateSubExpression(ctx);
        nesting.consolidateSubExpression(ctx);
    }

    // 构造函数初始化，对应int a(10);
    @Override
    public void enterInitDeclWithCall(CPP14Parser.InitDeclWithCallContext ctx) {
        CallExpression expr = new CallExpression();
        stack.push(expr);
    }

    @Override
    public void exitInitDeclWithCall(CPP14Parser.InitDeclWithCallContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }


    // 类静态变量 className::varName
    @Override
    public void enterClassIdentifier(CPP14Parser.ClassIdentifierContext ctx) {
        ClassStaticIdentifier staticVariable = new ClassStaticIdentifier();
        stack.push(staticVariable);
    }

    @Override
    public void exitQualifiedid(CPP14Parser.QualifiedidContext ctx) {
        if (stack.peek() instanceof ClassStaticIdentifier){
            ClassStaticIdentifier staticVariable = (ClassStaticIdentifier) stack.pop();
            staticVariable.initializeFromContext(ctx);
            stack.peek().addChild(staticVariable);
            if (!idType.empty() && idType.peek().equals(declarator))
                curVarNameId = staticVariable;
        }
    }

    // 三目表达式
    @Override
    public void enterRealConditionalExpression(CPP14Parser.RealConditionalExpressionContext ctx) {
        ConditionalExpression expr = new ConditionalExpression();
        stack.push(expr);
    }

    @Override
    public void exitRealConditionalExpression(CPP14Parser.RealConditionalExpressionContext ctx) {
        ConditionalExpression expr = (ConditionalExpression) stack.peek();
        ASTNode child = expr.getChild(0);
        if (child == null)
            return;
        Condition cnd = new Condition();
        cnd.addChild(child);
        cnd.initializeFromContext(child.parseTreeNodeContext);
        expr.replaceFirstChild(cnd);
        nesting.consolidateSubExpression(ctx);
    }

    // 首先是BinaryExpression
    // 赋值运算
    @Override
    public void enterAssignmentexpression(CPP14Parser.AssignmentexpressionContext ctx) {
        AssignmentExpr expr = new AssignmentExpr();
        stack.push(expr);
    }

    @Override
    public void exitAssignmentexpression(CPP14Parser.AssignmentexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 逻辑或运算：expr1 || expr2
    @Override
    public void enterLogicalorexpression(CPP14Parser.LogicalorexpressionContext ctx) {
        OrExpression expr = new OrExpression();
        stack.push(expr);
    }

    @Override
    public void exitLogicalorexpression(CPP14Parser.LogicalorexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    //逻辑与运算，expr1 && expr2
    @Override
    public void enterLogicalandexpression(CPP14Parser.LogicalandexpressionContext ctx) {
        AndExpression expr = new AndExpression();
        stack.push(expr);
    }

    @Override
    public void exitLogicalandexpression(CPP14Parser.LogicalandexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 与运算, expr1 | expr2
    @Override
    public void enterInclusiveorexpression(CPP14Parser.InclusiveorexpressionContext ctx) {
        InclusiveOrExpression expr = new InclusiveOrExpression();
        stack.push(expr);
    }

    @Override
    public void exitInclusiveorexpression(CPP14Parser.InclusiveorexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 异或运算, expr1 ^ expr2
    @Override
    public void enterExclusiveorexpression(CPP14Parser.ExclusiveorexpressionContext ctx) {
        ExclusiveOrExpression expr = new ExclusiveOrExpression();
        stack.push(expr);
    }

    @Override
    public void exitExclusiveorexpression(CPP14Parser.ExclusiveorexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 与运算, expr1 & expr2
    @Override
    public void enterAndexpression(CPP14Parser.AndexpressionContext ctx) {
        BitAndExpression expr = new BitAndExpression();
        stack.push(expr);
    }

    @Override
    public void exitAndexpression(CPP14Parser.AndexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 等于不等于判断，expr1 == expr2, expr1 != expr2
    @Override
    public void enterEqualityexpression(CPP14Parser.EqualityexpressionContext ctx) {
        EqualityExpression expr = new EqualityExpression();
        stack.push(expr);
    }

    @Override
    public void exitEqualityexpression(CPP14Parser.EqualityexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 大于小于判断，expr1 >= / <= / < / > expr2
    @Override
    public void enterRelationalexpression(CPP14Parser.RelationalexpressionContext ctx) {
        RelationalExpression expr = new RelationalExpression();
        stack.push(expr);
    }

    @Override
    public void exitRelationalexpression(CPP14Parser.RelationalexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 左移，右移运算 , expr1 << / >> expr2
    @Override
    public void enterShiftexpression(CPP14Parser.ShiftexpressionContext ctx) {
        ShiftExpression expr = new ShiftExpression();
        stack.push(expr);
    }

    @Override
    public void exitShiftexpression(CPP14Parser.ShiftexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 加减法运算, expr1 + / - expr2
    @Override
    public void enterAdditiveexpression(CPP14Parser.AdditiveexpressionContext ctx) {
        AdditiveExpression expr = new AdditiveExpression();
        stack.push(expr);
    }

    @Override
    public void exitAdditiveexpression(CPP14Parser.AdditiveexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 乘除模运算, expr1 * / / / % expr2
    @Override
    public void enterMultiplicativeexpression(CPP14Parser.MultiplicativeexpressionContext ctx) {
        MultiplicativeExpression expr = new MultiplicativeExpression();
        stack.push(expr);
    }

    @Override
    public void exitMultiplicativeexpression(CPP14Parser.MultiplicativeexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // New操作
    @Override
    public void enterNewexpression(CPP14Parser.NewexpressionContext ctx) {
        NewExpression newExpression = new NewExpression();
        stack.push(newExpression);
    }

    @Override
    public void exitNewexpression(CPP14Parser.NewexpressionContext ctx) {
        NewExpression newExpression = (NewExpression) stack.pop();
        newExpression.initializeFromContext(ctx);
        ASTNode topOfStack = stack.peek();
        topOfStack.addChild(newExpression);
        // nesting.consolidateSubExpression(ctx);
    }

    // Delete操作
    @Override
    public void enterDeleteexpression(CPP14Parser.DeleteexpressionContext ctx) {
        DeleteExpression deleteExpression = new DeleteExpression();
        stack.push(deleteExpression);
    }

    @Override
    public void exitDeleteexpression(CPP14Parser.DeleteexpressionContext ctx) {
        DeleteExpression deleteExpression = (DeleteExpression) stack.pop();
        deleteExpression.initializeFromContext(ctx);
        ASTNode topOfStack = stack.peek();
        topOfStack.addChild(deleteExpression);
    }

    // Throw操作
    @Override
    public void enterThrowexpression(CPP14Parser.ThrowexpressionContext ctx) {
        ThrowExpression throwExpression = new ThrowExpression();
        stack.push(throwExpression);
    }

    @Override
    public void exitThrowexpression(CPP14Parser.ThrowexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // new type id
    @Override
    public void enterNewtypeid(CPP14Parser.NewtypeidContext ctx) {
        curType = "";
        //idType.push(newTypeId);
    }

    @Override
    public void exitNewtypeid(CPP14Parser.NewtypeidContext ctx) {
        Identifier identifier = new Identifier();
        identifier.setCodeStr(curType);
        NewExpression newExpression = (NewExpression) stack.peek();
        newExpression.setTargetClass(identifier);
        curType = null;
        //idType.pop();
    }

    @Override
    public void enterPtrNewDeclarator(CPP14Parser.PtrNewDeclaratorContext ctx) {
        curType = curType.strip() + " *";
    }

    // Delete操作


    //CastTarget
    @Override
    public void enterThetypeid(CPP14Parser.ThetypeidContext ctx) {
        if (stack.peek() instanceof CastExpression){
            pastTheTypeId = true;
            CastTarget expr = new CastTarget();
            expr.setCodeStr("");
            stack.push(expr);
        }
        else if (stack.peek() instanceof SizeofExpr){
            SizeofOperand expr = new SizeofOperand();
            stack.push(expr);
        }
    }

    @Override
    public void exitThetypeid(CPP14Parser.ThetypeidContext ctx) {
        if (stack.peek() instanceof CastTarget){
            pastTheTypeId = false;
            nesting.consolidateSubExpression(ctx);
        }

        else if (stack.peek() instanceof SizeofOperand)
            nesting.consolidateSubExpression(ctx);
    }

    // 函数调用语句
    @Override
    public void enterFunctionCall(CPP14Parser.FunctionCallContext ctx) {
        CallExpression expr = new CallExpression();
        stack.push(expr);
    }

    @Override
    public void exitFunctionCall(CPP14Parser.FunctionCallContext ctx) {
        CallExpression expr = (CallExpression) stack.peek();
        ASTNode child = expr.getChild(0);
        if (child == null)
            return;
        Callee callee = new Callee();
        callee.setCodeStr(child.getEscapedCodeStr());
        callee.addChild(child);
        expr.replaceFirstChild(callee);
        // 如果没有调用参数，就创建一个空的参数列表
        if (expr.getArgumentList() == null){
            ArgumentList argumentList = new ArgumentList();
            argumentList.setCodeStr("");
            expr.setArgumentList(argumentList);
        }
        nesting.consolidateSubExpression(ctx);
    }

    // CastExpression
    @Override
    public void enterCastexpression(CPP14Parser.CastexpressionContext ctx) {
        CastExpression expr = new CastExpression();
        stack.push(expr);
    }

    @Override
    public void exitCastexpression(CPP14Parser.CastexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    @Override
    public void enterPostSimpleCastExpression(CPP14Parser.PostSimpleCastExpressionContext ctx) {
        CastExpression expr = new CastExpression();
        stack.push(expr);
    }

    @Override
    public void exitPostSimpleCastExpression(CPP14Parser.PostSimpleCastExpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // cast target
    @Override
    public void enterSimpletypespecifier(CPP14Parser.SimpletypespecifierContext ctx) {
        // type(n) cast
        if (!pastTheTypeId && stack.peek() instanceof CastExpression){
            CastTarget expr = new CastTarget();
            stack.push(expr);
        }
    }

    @Override
    public void exitSimpletypespecifier(CPP14Parser.SimpletypespecifierContext ctx) {
        if (!pastTheTypeId && stack.peek() instanceof CastTarget)
            nesting.consolidateSubExpression(ctx);
    }

    @Override
    public void enterCppCastExpression(CPP14Parser.CppCastExpressionContext ctx) {
        CastExpression expr = new CastExpression();
        stack.push(expr);
    }

    @Override
    public void exitCppCastExpression(CPP14Parser.CppCastExpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // Sizeof
    @Override
    public void enterSizeofExpression(CPP14Parser.SizeofExpressionContext ctx) {
        SizeofExpr expr = new SizeofExpr();
        stack.push(expr);
    }

    @Override
    public void exitSizeofExpression(CPP14Parser.SizeofExpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // expressionlist父节点可能为newExpression, meminitializer, initializer，postfix
    @Override
    public void enterExpressionlist(CPP14Parser.ExpressionlistContext ctx) {
        // 父节点是函数调用
        if (stack.peek() instanceof CallExpression){
            ArgumentList expr = new ArgumentList();
            stack.push(expr);
        }
    }

    @Override
    public void exitExpressionlist(CPP14Parser.ExpressionlistContext ctx) {
        // 当前结点是参数列表
        if (stack.peek() instanceof ArgumentList)
            nesting.consolidateSubExpression(ctx);
    }

    // 普通赋值语句 var = literal;
    @Override
    public void enterNormalAssign(CPP14Parser.NormalAssignContext ctx) {
        // 父节点是函数调用参数列表
        if (stack.peek() instanceof ArgumentList){
            Argument expr = new Argument();
            stack.push(expr);
        }
    }

    @Override
    public void exitNormalAssign(CPP14Parser.NormalAssignContext ctx) {
        if (stack.peek() instanceof Argument)
            nesting.consolidateSubExpression(ctx);
    }

    // 列表赋值语句 var = { literal1, literal2, ... }
    @Override
    public void enterArrayAssign(CPP14Parser.ArrayAssignContext ctx) {
        InitializerList initializerList = new InitializerList();
        stack.push(initializerList);
    }

    @Override
    public void exitArrayAssign(CPP14Parser.ArrayAssignContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 后缀表达式，x++, x--
    @Override
    public void enterIncDecOp(CPP14Parser.IncDecOpContext ctx) {
        IncDecOp incDecOp = new IncDecOp();
        stack.push(incDecOp);
    }

    @Override
    public void exitIncDecOp(CPP14Parser.IncDecOpContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 单目运算符, -1
    @Override
    public void enterUnaryexpression(CPP14Parser.UnaryexpressionContext ctx) {
        UnaryOp unaryOp = new UnaryOp();
        stack.push(unaryOp);
    }

    @Override
    public void exitUnaryexpression(CPP14Parser.UnaryexpressionContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 结构体属性访问
    @Override
    public void enterMemberAccess(CPP14Parser.MemberAccessContext ctx) {
        MemberAccess expr = new MemberAccess();
        stack.push(expr);
    }

    @Override
    public void exitMemberAccess(CPP14Parser.MemberAccessContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    @Override
    public void enterPtrMemberAccess(CPP14Parser.PtrMemberAccessContext ctx) {
        PtrMemberAccess expr = new PtrMemberAccess();
        stack.push(expr);
    }

    @Override
    public void exitPtrMemberAccess(CPP14Parser.PtrMemberAccessContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }

    // 数组访问
    @Override
    public void enterArrayIndexing(CPP14Parser.ArrayIndexingContext ctx) {
        ArrayIndexing expr = new ArrayIndexing();
        stack.push(expr);
    }

    @Override
    public void exitArrayIndexing(CPP14Parser.ArrayIndexingContext ctx) {
        nesting.consolidateSubExpression(ctx);
    }



    // 处理终端结点
    @Override
    public void visitTerminal(TerminalNode node) {
        ASTNode parent = stack.peek();
        // 当前终端结点属于变量定义语句一部分并且不属于C++模板定义
        if (!pastTheTypeId && !idType.empty() && idType.peek().equals(varDecl)){
            // 普通的变量定义初始化
            if (curType == null)
                return;
            curType += node.getText() + " ";
            return;
        }

        if (parent instanceof CastTarget){
            parent.setCodeStr(parent.getEscapedCodeStr().strip() + " " + node.getText());
            return;
        }

        switch (node.getSymbol().getType()){
            // sizeof运算符
            case CPP14Parser.Sizeof:
                Sizeof op = new Sizeof();
                op.setCodeStr(node.getText());
                parent.addChild(op);
                break;
            // 字面量
            case CPP14Parser.Identifier:
                Identifier expr = new Identifier();
                expr.setCodeStr(node.getText());
                parent.addChild(expr);
                // 如果当前Identifier在变量定义语句中
                if (!idType.empty() && idType.peek().equals(declarator))
                    curVarNameId = expr;
                break;

            case CPP14Parser.Integerliteral:
            case CPP14Parser.Userdefinedintegerliteral:
                IntegerExpression integerExpression = new IntegerExpression();
                integerExpression.setCodeStr(node.getText());
                parent.addChild(integerExpression);
                break;

            case CPP14Parser.Floatingliteral:
            case CPP14Parser.Userdefinedfloatingliteral:
                DoubleExpression doubleExpression = new DoubleExpression();
                doubleExpression.setCodeStr(node.getText());
                parent.addChild(doubleExpression);
                break;

            case CPP14Parser.Characterliteral:
            case CPP14Parser.Userdefinedcharacterliteral:
                CharExpression charExpression = new CharExpression();
                charExpression.setCodeStr(node.getText());
                parent.addChild(charExpression);
                break;

            case CPP14Parser.Stringliteral:
            case CPP14Parser.Userdefinedstringliteral:
                StringExpression stringExpression = new StringExpression();
                stringExpression.setCodeStr(node.getText());
                parent.addChild(stringExpression);
                break;

            case CPP14Parser.True:
            case CPP14Parser.False:
                BoolExpression boolExpression = new BoolExpression();
                boolExpression.setCodeStr(node.getText());
                parent.addChild(boolExpression);
                break;

            case CPP14Parser.Nullptr:
                PointerExpression pointerExpression = new PointerExpression();
                pointerExpression.setCodeStr(node.getText());
                parent.addChild(pointerExpression);
                break;

            // 单目运算符 &, *, +, -, ~, !
            case CPP14Parser.Star: // *expr
            case CPP14Parser.And: // &expr
            case CPP14Parser.Plus: // +expr
            case CPP14Parser.Minus: // -expr
            case CPP14Parser.Tilde: // ~expr
            case CPP14Parser.Not: // !expr
                //当前是单目运算
                if (parent instanceof UnaryOp) {
                    UnaryOperator unaryOperator = new UnaryOperator();
                    unaryOperator.setOperator(node.getText());
                    unaryOperator.setCodeStr(node.getText());
                    parent.addChild(unaryOperator);
                }
                break;

            case CPP14Parser.PlusPlus: // ++expr 或 expr++
            case CPP14Parser.MinusMinus: // --expr 或 expr --
                IncDec incDec = new IncDec();
                incDec.setOperator(node.getText());
                incDec.setCodeStr(node.getText());
                if (parent instanceof UnaryOp){
                    IncDecOp incDecOp = new IncDecOp();
                    // ++x或者 --x
                    incDecOp.isPost = false;
                    incDecOp.setOperator(incDec.getOperator());
                    incDecOp.addChild(incDec);
                    replaceTopOfStack(incDecOp);
                }

                else if (parent instanceof IncDecOp){
                    ((IncDecOp) parent).setOperator(incDec.getOperator());
                    parent.addChild(incDec);
                }

                break;
        }
    }


}
