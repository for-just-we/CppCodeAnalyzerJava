package parsing.CPP;

import antlr.Cpp.CPP14Parser;
import ast.statements.CompoundStatement;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.misc.Interval;

public class ModuleFunctionParserInterface {
    // Extracts compound statement from input stream
    // as a string and passes that string to the
    // function parser. The resulting 'CompoundStatement'
    // (an AST node) is returned.


    private static String getCompoundStmtAsString(CPP14Parser.FunctiondefinitionContext ctx)
    {
        CPP14Parser.CompoundstatementContext compound_statement = ctx.functionbody().compoundstatement();
        CharStream inputStream = compound_statement.start.getInputStream();
        int startIndex = compound_statement.start.getStopIndex();
        int stopIndex = compound_statement.stop.getStopIndex();
        return inputStream.getText(new Interval(startIndex + 1, stopIndex - 1));
    }
}
