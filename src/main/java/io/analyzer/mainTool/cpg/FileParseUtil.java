package io.analyzer.mainTool.cpg;

import io.analyzer.mainTool.antlr.Cpp.CPP14Lexer;
import io.analyzer.mainTool.antlr.Cpp.CPP14Parser;
import io.analyzer.mainTool.ddg.CFGAndUDGToDefUseCFG;
import io.analyzer.mainTool.ddg.DataDependenceGraph.DDG;
import io.analyzer.mainTool.ddg.DefUseCFG.DefUseCFG;
import io.analyzer.mainTool.parsing.CPP.builder.FileBuilder;
import io.analyzer.mainTool.udg.useDefGraph.CFGToUDGConverter;
import io.analyzer.mainTool.udg.useDefGraph.UseDefGraph;
import io.analyzer.mainTool.ast.declaration.ClassDefStatement;
import io.analyzer.mainTool.ast.functionDef.FunctionDef;
import io.analyzer.mainTool.cdg.CDG;
import io.analyzer.mainTool.cdg.CDGCreator;
import io.analyzer.mainTool.cfg.ASTToCFGConverter;
import io.analyzer.mainTool.cfg.CFG;
import io.analyzer.mainTool.ddg.DDGCreator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileParseUtil {
    private ASTToCFGConverter cfgConverter;
    private CFGToUDGConverter udgConverter;
    private CFGAndUDGToDefUseCFG defUseCFGConverter;
    private DDGCreator ddgCreator;
    private CDGCreator cdgCreator;

    public FileParseUtil(ASTToCFGConverter cfgConverter, CFGToUDGConverter udgConverter, CFGAndUDGToDefUseCFG defUseCFGConverter,
                         DDGCreator ddgCreator, CDGCreator cdgCreator) {
        this.cfgConverter = cfgConverter;
        this.udgConverter = udgConverter;
        this.defUseCFGConverter = defUseCFGConverter;
        this.ddgCreator = ddgCreator;
        this.cdgCreator = cdgCreator;
    }

    public void clear(){
        this.ddgCreator.clear();
    }

    public CPG convertASTtoCPG(FunctionDef functionDef){
        CFG cfg = cfgConverter.convert(functionDef);
        UseDefGraph useDefGraph = udgConverter.convert(cfg);
        DefUseCFG defUseCFG = defUseCFGConverter.convert(cfg, useDefGraph);
        DDG ddg = ddgCreator.createForDefUseCFG(defUseCFG);
        CDG cdg = cdgCreator.create(cfg);

        CPG cpg = new CPG();
        cpg.initCFGEdges(cfg);
        cpg.initCDGEdges(cdg);
        cpg.initDDGEdges(ddg);
        cpg.funcName = cfg.getName();
        return cpg;
    }

    public List<CPG> fileParsing(String fileName)
            throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(fileName));
        CPP14Lexer lexer = new CPP14Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CPP14Parser parser = new CPP14Parser(tokens);
        ParseTree tree = parser.translationunit();
        FileBuilder builder = new FileBuilder();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(builder, tree);

        List<FunctionDef> functions = new ArrayList<>();
        functions.addAll(builder.functionDefs);
        for (ClassDefStatement classDecl: builder.classDefs)
            functions.addAll(classDecl.functionDefs);

        List<CPG> cpgs = functions.stream().map(functionDef -> {
            CPG cpg = convertASTtoCPG(functionDef);
            cpg.fileName = fileName;
            return cpg;
        }).collect(Collectors.toList());

        return cpgs;
    }
}
