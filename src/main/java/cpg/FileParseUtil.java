package cpg;

import antlr.Cpp.CPP14Lexer;
import antlr.Cpp.CPP14Parser;
import ast.declaration.ClassDefStatement;
import ast.functionDef.FunctionDef;
import cdg.CDG;
import cdg.CDGCreator;
import cfg.ASTToCFGConverter;
import cfg.CFG;
import ddg.CFGAndUDGToDefUseCFG;
import ddg.DDGCreator;
import ddg.DataDependenceGraph.DDG;
import ddg.DefUseCFG.DefUseCFG;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import parsing.CPP.builder.FileBuilder;
import udg.useDefGraph.CFGToUDGConverter;
import udg.useDefGraph.UseDefGraph;

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
