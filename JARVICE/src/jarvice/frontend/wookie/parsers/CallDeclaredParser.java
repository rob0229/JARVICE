package jarvice.frontend.wookie.parsers;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;

import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;


public class CallDeclaredParser extends CallParser
{
 
    public CallDeclaredParser(WookieParserTD parent)
    {
        super(parent);
    }

    public ICodeNode parse(Token token)
        throws Exception
    {
        // Create the CALL node.
        ICodeNode callNode = ICodeFactory.createICodeNode(CALL);
        SymTabEntry pfId = symTabStack.lookup(token.getText().toLowerCase());
        callNode.setAttribute(ID, pfId);
        callNode.setTypeSpec(pfId.getTypeSpec());
        token = nextToken();  // consume procedure or function identifier
        ICodeNode parmsNode = parseActualParameters(token, pfId,
                                                    true, false, false);
        callNode.addChild(parmsNode);
        return callNode;
    }
}
