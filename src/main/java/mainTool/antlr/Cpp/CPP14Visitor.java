// Generated from D:/projects/Java/CodeParser/src/main/resources\CPP14.g4 by ANTLR 4.10.1
package mainTool.antlr.Cpp;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CPP14Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CPP14Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#translationunit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTranslationunit(CPP14Parser.TranslationunitContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#primaryexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryexpression(CPP14Parser.PrimaryexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#idexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdexpression(CPP14Parser.IdexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#unqualifiedid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#qualifiedid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedid(CPP14Parser.QualifiedidContext ctx);
	/**
	 * Visit a parse tree produced by the {@code normalGlobalIdentifier}
	 * labeled alternative in {@link CPP14Parser#nestednamespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormalGlobalIdentifier(CPP14Parser.NormalGlobalIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classIdentifier}
	 * labeled alternative in {@link CPP14Parser#nestednamespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassIdentifier(CPP14Parser.ClassIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nestedIdentifier}
	 * labeled alternative in {@link CPP14Parser#nestednamespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedIdentifier(CPP14Parser.NestedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code namespaceIdentifier}
	 * labeled alternative in {@link CPP14Parser#nestednamespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceIdentifier(CPP14Parser.NamespaceIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nestedTemplateIdentifier}
	 * labeled alternative in {@link CPP14Parser#nestednamespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedTemplateIdentifier(CPP14Parser.NestedTemplateIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decltypeIdentifier}
	 * labeled alternative in {@link CPP14Parser#nestednamespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecltypeIdentifier(CPP14Parser.DecltypeIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#lambdaexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaexpression(CPP14Parser.LambdaexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#lambdaintroducer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaintroducer(CPP14Parser.LambdaintroducerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#lambdacapture}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdacapture(CPP14Parser.LambdacaptureContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#capturedefault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCapturedefault(CPP14Parser.CapturedefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#capturelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCapturelist(CPP14Parser.CapturelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#capture}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCapture(CPP14Parser.CaptureContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simplecapture}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimplecapture(CPP14Parser.SimplecaptureContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initcapture}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitcapture(CPP14Parser.InitcaptureContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#lambdadeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdadeclarator(CPP14Parser.LambdadeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberAccess}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberAccess(CPP14Parser.MemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code postTypeCastExpression}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostTypeCastExpression(CPP14Parser.PostTypeCastExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code incDecOp}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncDecOp(CPP14Parser.IncDecOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeidExpression}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeidExpression(CPP14Parser.TypeidExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(CPP14Parser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code postSimpleCastExpression}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostSimpleCastExpression(CPP14Parser.PostSimpleCastExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CppCastExpression}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCppCastExpression(CPP14Parser.CppCastExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayIndexing}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIndexing(CPP14Parser.ArrayIndexingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ptrMemberAccess}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPtrMemberAccess(CPP14Parser.PtrMemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code postfixIgnore}
	 * labeled alternative in {@link CPP14Parser#postfixexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixIgnore(CPP14Parser.PostfixIgnoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#expressionlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionlist(CPP14Parser.ExpressionlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#pseudodestructorname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudodestructorname(CPP14Parser.PseudodestructornameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#unaryexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryexpression(CPP14Parser.UnaryexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#sizeofExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSizeofExpression(CPP14Parser.SizeofExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#alignofExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlignofExpression(CPP14Parser.AlignofExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#unaryoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryoperator(CPP14Parser.UnaryoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#newexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewexpression(CPP14Parser.NewexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#newplacement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewplacement(CPP14Parser.NewplacementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#newtypeid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewtypeid(CPP14Parser.NewtypeidContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ptrNewDeclarator}
	 * labeled alternative in {@link CPP14Parser#newdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPtrNewDeclarator(CPP14Parser.PtrNewDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nonPtrNewDeclarator}
	 * labeled alternative in {@link CPP14Parser#newdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonPtrNewDeclarator(CPP14Parser.NonPtrNewDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noptrnewdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoptrnewdeclarator(CPP14Parser.NoptrnewdeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#newinitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewinitializer(CPP14Parser.NewinitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#deleteexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteexpression(CPP14Parser.DeleteexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noexceptexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoexceptexpression(CPP14Parser.NoexceptexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#castexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastexpression(CPP14Parser.CastexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#pmexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPmexpression(CPP14Parser.PmexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#multiplicativeexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeexpression(CPP14Parser.MultiplicativeexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#additiveexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveexpression(CPP14Parser.AdditiveexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#shiftexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftexpression(CPP14Parser.ShiftexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#relationalexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalexpression(CPP14Parser.RelationalexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#equalityexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityexpression(CPP14Parser.EqualityexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#andexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndexpression(CPP14Parser.AndexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#exclusiveorexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusiveorexpression(CPP14Parser.ExclusiveorexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#inclusiveorexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclusiveorexpression(CPP14Parser.InclusiveorexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#logicalandexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalandexpression(CPP14Parser.LogicalandexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#logicalorexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalorexpression(CPP14Parser.LogicalorexpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nonConditionalExpression}
	 * labeled alternative in {@link CPP14Parser#conditionalexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonConditionalExpression(CPP14Parser.NonConditionalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code realConditionalExpression}
	 * labeled alternative in {@link CPP14Parser#conditionalexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealConditionalExpression(CPP14Parser.RealConditionalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#assignmentexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentexpression(CPP14Parser.AssignmentexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#assignmentoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentoperator(CPP14Parser.AssignmentoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(CPP14Parser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#constantexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantexpression(CPP14Parser.ConstantexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(CPP14Parser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(CPP14Parser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#labeledstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabeledstatement(CPP14Parser.LabeledstatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#expressionstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionstatement(CPP14Parser.ExpressionstatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#compoundstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundstatement(CPP14Parser.CompoundstatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#statementseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementseq(CPP14Parser.StatementseqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link CPP14Parser#selectionstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(CPP14Parser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code switchStatement}
	 * labeled alternative in {@link CPP14Parser#selectionstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchStatement(CPP14Parser.SwitchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(CPP14Parser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link CPP14Parser#iterationstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(CPP14Parser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DoStatement}
	 * labeled alternative in {@link CPP14Parser#iterationstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoStatement(CPP14Parser.DoStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link CPP14Parser#iterationstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(CPP14Parser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForRangeStatement}
	 * labeled alternative in {@link CPP14Parser#iterationstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForRangeStatement(CPP14Parser.ForRangeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#forinitstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForinitstatement(CPP14Parser.ForinitstatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#forrangedeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForrangedeclaration(CPP14Parser.ForrangedeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#forrangeinitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForrangeinitializer(CPP14Parser.ForrangeinitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#jumpstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpstatement(CPP14Parser.JumpstatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declarationstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationstatement(CPP14Parser.DeclarationstatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declarationseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationseq(CPP14Parser.DeclarationseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(CPP14Parser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#blockdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockdeclaration(CPP14Parser.BlockdeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#aliasdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasdeclaration(CPP14Parser.AliasdeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simpledeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#static_assertdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatic_assertdeclaration(CPP14Parser.Static_assertdeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#emptydeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptydeclaration(CPP14Parser.EmptydeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributedeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributedeclaration(CPP14Parser.AttributedeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code storageAttr}
	 * labeled alternative in {@link CPP14Parser#declspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorageAttr(CPP14Parser.StorageAttrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeAttr}
	 * labeled alternative in {@link CPP14Parser#declspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAttr(CPP14Parser.TypeAttrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcAttr}
	 * labeled alternative in {@link CPP14Parser#declspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncAttr(CPP14Parser.FuncAttrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code friendDecl}
	 * labeled alternative in {@link CPP14Parser#declspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFriendDecl(CPP14Parser.FriendDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typedefDecl}
	 * labeled alternative in {@link CPP14Parser#declspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefDecl(CPP14Parser.TypedefDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constExprDecl}
	 * labeled alternative in {@link CPP14Parser#declspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstExprDecl(CPP14Parser.ConstExprDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declspecifierseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclspecifierseq(CPP14Parser.DeclspecifierseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#storageclassspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorageclassspecifier(CPP14Parser.StorageclassspecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#functionspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionspecifier(CPP14Parser.FunctionspecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typedefname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefname(CPP14Parser.TypedefnameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code otherDecl}
	 * labeled alternative in {@link CPP14Parser#typespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOtherDecl(CPP14Parser.OtherDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classDecl}
	 * labeled alternative in {@link CPP14Parser#typespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDecl(CPP14Parser.ClassDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code enumDecl}
	 * labeled alternative in {@link CPP14Parser#typespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDecl(CPP14Parser.EnumDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#trailingtypespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailingtypespecifier(CPP14Parser.TrailingtypespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typespecifierseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypespecifierseq(CPP14Parser.TypespecifierseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#trailingtypespecifierseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailingtypespecifierseq(CPP14Parser.TrailingtypespecifierseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simpletypespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpletypespecifier(CPP14Parser.SimpletypespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#thetypename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThetypename(CPP14Parser.ThetypenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#decltypespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecltypespecifier(CPP14Parser.DecltypespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#elaboratedtypespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElaboratedtypespecifier(CPP14Parser.ElaboratedtypespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumname(CPP14Parser.EnumnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumspecifier(CPP14Parser.EnumspecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumhead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumhead(CPP14Parser.EnumheadContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#opaqueenumdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpaqueenumdeclaration(CPP14Parser.OpaqueenumdeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumkey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumkey(CPP14Parser.EnumkeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumbase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumbase(CPP14Parser.EnumbaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumeratorlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumeratorlist(CPP14Parser.EnumeratorlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumeratordefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumeratordefinition(CPP14Parser.EnumeratordefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumerator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumerator(CPP14Parser.EnumeratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namespacename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespacename(CPP14Parser.NamespacenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#originalnamespacename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOriginalnamespacename(CPP14Parser.OriginalnamespacenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namespacedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespacedefinition(CPP14Parser.NamespacedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namednamespacedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamednamespacedefinition(CPP14Parser.NamednamespacedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#originalnamespacedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOriginalnamespacedefinition(CPP14Parser.OriginalnamespacedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#extensionnamespacedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtensionnamespacedefinition(CPP14Parser.ExtensionnamespacedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#unnamednamespacedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnnamednamespacedefinition(CPP14Parser.UnnamednamespacedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namespacebody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespacebody(CPP14Parser.NamespacebodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namespacealias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespacealias(CPP14Parser.NamespacealiasContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namespacealiasdefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespacealiasdefinition(CPP14Parser.NamespacealiasdefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#qualifiednamespacespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiednamespacespecifier(CPP14Parser.QualifiednamespacespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#usingdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsingdeclaration(CPP14Parser.UsingdeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#usingdirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsingdirective(CPP14Parser.UsingdirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#asmdefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsmdefinition(CPP14Parser.AsmdefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#linkagespecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkagespecification(CPP14Parser.LinkagespecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributespecifierseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributespecifierseq(CPP14Parser.AttributespecifierseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributespecifier(CPP14Parser.AttributespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#alignmentspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlignmentspecifier(CPP14Parser.AlignmentspecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributelist(CPP14Parser.AttributelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(CPP14Parser.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributetoken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributetoken(CPP14Parser.AttributetokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributescopedtoken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributescopedtoken(CPP14Parser.AttributescopedtokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributenamespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributenamespace(CPP14Parser.AttributenamespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributeargumentclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeargumentclause(CPP14Parser.AttributeargumentclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#balancedtokenseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBalancedtokenseq(CPP14Parser.BalancedtokenseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#balancedtoken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBalancedtoken(CPP14Parser.BalancedtokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initdeclaratorlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitdeclaratorlist(CPP14Parser.InitdeclaratorlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitdeclarator(CPP14Parser.InitdeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarator(CPP14Parser.DeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nonPtrDecl}
	 * labeled alternative in {@link CPP14Parser#ptrdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonPtrDecl(CPP14Parser.NonPtrDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ptrDecl}
	 * labeled alternative in {@link CPP14Parser#ptrdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPtrDecl(CPP14Parser.PtrDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code noptrIgnore}
	 * labeled alternative in {@link CPP14Parser#noptrdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoptrIgnore(CPP14Parser.NoptrIgnoreContext ctx);
	/**
	 * Visit a parse tree produced by the {@code normalVarDecl}
	 * labeled alternative in {@link CPP14Parser#noptrdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormalVarDecl(CPP14Parser.NormalVarDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayDecl}
	 * labeled alternative in {@link CPP14Parser#noptrdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayDecl(CPP14Parser.ArrayDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#parametersandqualifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#trailingreturntype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailingreturntype(CPP14Parser.TrailingreturntypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#ptroperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPtroperator(CPP14Parser.PtroperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#cvqualifierseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCvqualifierseq(CPP14Parser.CvqualifierseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#cvqualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCvqualifier(CPP14Parser.CvqualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#refqualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefqualifier(CPP14Parser.RefqualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declaratorid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaratorid(CPP14Parser.DeclaratoridContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#thetypeid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThetypeid(CPP14Parser.ThetypeidContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#abstractdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstractdeclarator(CPP14Parser.AbstractdeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#ptrabstractdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPtrabstractdeclarator(CPP14Parser.PtrabstractdeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noptrabstractdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoptrabstractdeclarator(CPP14Parser.NoptrabstractdeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#abstractpackdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstractpackdeclarator(CPP14Parser.AbstractpackdeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noptrabstractpackdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoptrabstractpackdeclarator(CPP14Parser.NoptrabstractpackdeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#parameterdeclarationclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterdeclarationclause(CPP14Parser.ParameterdeclarationclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#parameterdeclarationlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterdeclarationlist(CPP14Parser.ParameterdeclarationlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#parameterdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterdeclaration(CPP14Parser.ParameterdeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#functiondefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#functionbody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionbody(CPP14Parser.FunctionbodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code initDeclWithAssignList}
	 * labeled alternative in {@link CPP14Parser#initializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitDeclWithAssignList(CPP14Parser.InitDeclWithAssignListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code initDeclWithCall}
	 * labeled alternative in {@link CPP14Parser#initializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitDeclWithCall(CPP14Parser.InitDeclWithCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code initDeclWithAssign}
	 * labeled alternative in {@link CPP14Parser#braceorequalinitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitDeclWithAssign(CPP14Parser.InitDeclWithAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code initDeclWithList}
	 * labeled alternative in {@link CPP14Parser#braceorequalinitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitDeclWithList(CPP14Parser.InitDeclWithListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code normalAssign}
	 * labeled alternative in {@link CPP14Parser#initializerclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormalAssign(CPP14Parser.NormalAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayAssign}
	 * labeled alternative in {@link CPP14Parser#initializerclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayAssign(CPP14Parser.ArrayAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initializerlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitializerlist(CPP14Parser.InitializerlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#bracedinitlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracedinitlist(CPP14Parser.BracedinitlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassname(CPP14Parser.ClassnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassspecifier(CPP14Parser.ClassspecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classhead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClasshead(CPP14Parser.ClassheadContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classheadname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassheadname(CPP14Parser.ClassheadnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classvirtspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassvirtspecifier(CPP14Parser.ClassvirtspecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classkey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClasskey(CPP14Parser.ClasskeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#memberspecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberspecification(CPP14Parser.MemberspecificationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberVarDecl}
	 * labeled alternative in {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberVarDecl(CPP14Parser.MemberVarDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberFuncDecl}
	 * labeled alternative in {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberFuncDecl(CPP14Parser.MemberFuncDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberUsing}
	 * labeled alternative in {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberUsing(CPP14Parser.MemberUsingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberStaticAssert}
	 * labeled alternative in {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberStaticAssert(CPP14Parser.MemberStaticAssertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberTemplateDecl}
	 * labeled alternative in {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberTemplateDecl(CPP14Parser.MemberTemplateDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberAliasDecl}
	 * labeled alternative in {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberAliasDecl(CPP14Parser.MemberAliasDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberEmpty}
	 * labeled alternative in {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberEmpty(CPP14Parser.MemberEmptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#memberdeclaratorlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberdeclaratorlist(CPP14Parser.MemberdeclaratorlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#memberdeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberdeclarator(CPP14Parser.MemberdeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#virtspecifierseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVirtspecifierseq(CPP14Parser.VirtspecifierseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#virtspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVirtspecifier(CPP14Parser.VirtspecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#purespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPurespecifier(CPP14Parser.PurespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#baseclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseclause(CPP14Parser.BaseclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#basespecifierlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasespecifierlist(CPP14Parser.BasespecifierlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#basespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasespecifier(CPP14Parser.BasespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classordecltype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassordecltype(CPP14Parser.ClassordecltypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#basetypespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasetypespecifier(CPP14Parser.BasetypespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#accessspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessspecifier(CPP14Parser.AccessspecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#conversionfunctionid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversionfunctionid(CPP14Parser.ConversionfunctionidContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#conversiontypeid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversiontypeid(CPP14Parser.ConversiontypeidContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#conversiondeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversiondeclarator(CPP14Parser.ConversiondeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#ctorinitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCtorinitializer(CPP14Parser.CtorinitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#meminitializerlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeminitializerlist(CPP14Parser.MeminitializerlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#meminitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeminitializer(CPP14Parser.MeminitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#meminitializerid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeminitializerid(CPP14Parser.MeminitializeridContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#operatorfunctionid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorfunctionid(CPP14Parser.OperatorfunctionidContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#literaloperatorid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteraloperatorid(CPP14Parser.LiteraloperatoridContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templatedeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplatedeclaration(CPP14Parser.TemplatedeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateparameterlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateparameterlist(CPP14Parser.TemplateparameterlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateparameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateparameter(CPP14Parser.TemplateparameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typeparameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeparameter(CPP14Parser.TypeparameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simpletemplateid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpletemplateid(CPP14Parser.SimpletemplateidContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateid(CPP14Parser.TemplateidContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templatename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplatename(CPP14Parser.TemplatenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateargumentlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateargumentlist(CPP14Parser.TemplateargumentlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateargument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateargument(CPP14Parser.TemplateargumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typenamespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypenamespecifier(CPP14Parser.TypenamespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#explicitinstantiation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitinstantiation(CPP14Parser.ExplicitinstantiationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#explicitspecialization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitspecialization(CPP14Parser.ExplicitspecializationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#tryblock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryblock(CPP14Parser.TryblockContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#functiontryblock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctiontryblock(CPP14Parser.FunctiontryblockContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#handlerseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerseq(CPP14Parser.HandlerseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#handler}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandler(CPP14Parser.HandlerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#exceptiondeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptiondeclaration(CPP14Parser.ExceptiondeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#throwexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowexpression(CPP14Parser.ThrowexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#exceptionspecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionspecification(CPP14Parser.ExceptionspecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#dynamicexceptionspecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDynamicexceptionspecification(CPP14Parser.DynamicexceptionspecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typeidlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeidlist(CPP14Parser.TypeidlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noexceptspecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoexceptspecification(CPP14Parser.NoexceptspecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#rightShift}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRightShift(CPP14Parser.RightShiftContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#rightShiftAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRightShiftAssign(CPP14Parser.RightShiftAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#theoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTheoperator(CPP14Parser.TheoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(CPP14Parser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#booleanliteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanliteral(CPP14Parser.BooleanliteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#pointerliteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointerliteral(CPP14Parser.PointerliteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#userdefinedliteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserdefinedliteral(CPP14Parser.UserdefinedliteralContext ctx);
}