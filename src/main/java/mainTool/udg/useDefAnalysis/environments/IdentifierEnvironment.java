package mainTool.udg.useDefAnalysis.environments;

import java.util.LinkedList;

public class IdentifierEnvironment extends UseDefEnvironment{
    // pass the 'code' of the identifier up-stream
    // Identifier类型直接获取token作为symbol
    @Override
    public LinkedList<String> upstreamSymbols() {
        String code = astProvider.getEscapedCodeStr();
        LinkedList<String> retval = new LinkedList<>();
        retval.add(code);
        return retval;
    }
}
