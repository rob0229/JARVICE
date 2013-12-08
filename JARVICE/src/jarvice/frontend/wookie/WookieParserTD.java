package jarvice.frontend.wookie;

import java.util.ArrayList;
import java.util.EnumSet;

import jarvice.frontend.Token;
import jarvice.frontend.wookie.parsers.DeclaredRoutineParser;
import jarvice.intermediate.symtabimpl.DefinitionImpl;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.ROUTINE_ICODE;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.ROUTINE_ROUTINES;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.ROUTINE_SYMTAB;
import jarvice.intermediate.symtabimpl.Predefined;
import jarvice.message.Message;
import jarvice.frontend.*;
import jarvice.intermediate.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.message.MessageType.PARSER_SUMMARY;
import static jarvice.frontend.wookie.WookieErrorCode.IO_ERROR;

public class WookieParserTD extends Parser {
	protected static WookieErrorHandler errorHandler = new WookieErrorHandler();
	
	public SymTabEntry routineId; // name of the routine being parsed

	public WookieParserTD(Scanner scanner) {
		super(scanner);
	}

	public WookieParserTD(WookieParserTD parent) {
		super(parent.getScanner());
	}
	
	public WookieErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void parse() throws Exception {
			 
		 long startTime = System.currentTimeMillis();
	        Predefined.initialize(symTabStack);
	        SymTabEntry routineId = symTabStack.enterLocal("c code");
	        routineId.setDefinition(DefinitionImpl.PROGRAM);

            // create intermediate code for calling main()
            ICode iCode = ICodeFactory.createICode();
            routineId.setAttribute(ROUTINE_ICODE, iCode);
            routineId.setAttribute(ROUTINE_ROUTINES, new ArrayList<SymTabEntry>());

            // push symbol table onto stack
            routineId.setAttribute(ROUTINE_SYMTAB, symTabStack.push());

            // set program identifier in symbol table stack
            symTabStack.setProgramId(routineId);
           
             symTabStack.getLocalSymTab().nextSlotNumber();
            
            // parse routines
	        
	        
	        
	        
	        try {	        	
	            Token token = nextToken();		
	            DeclaredRoutineParser routineParser = new DeclaredRoutineParser(this);
	            
	        	// Parse the program.
	          do {     
	            routineParser.parse(token, routineId);
	            token = currentToken();        
	          } while (token.getType() == INT ||token.getType() == CHAR ||token.getType() == VOID);
	          
	            // Send the parser summary message.
	            float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
	            sendMessage(new Message(PARSER_SUMMARY, new Number[] {token.getLineNumber(), getErrorCount(), elapsedTime}));
	        }
	        catch (java.io.IOException ex) {
	            errorHandler.abortTranslation(IO_ERROR, this);
	        }		
	}

	/**
	 * Return the number of syntax errors found by the parser.
	 * 
	 * @return the error count.
	 */
	public int getErrorCount() {
		return errorHandler.getErrorCount();
	}

	public Token synchronize(EnumSet syncSet) throws Exception {
		Token token = currentToken();

		// If the current token is not in the synchronization set,
		// then it is unexpected and the parser must recover.
		if (!syncSet.contains(token.getType())) {

			// Flag the unexpected token.
			errorHandler.flag(token, UNEXPECTED_TOKEN, this);

			// Recover by skipping tokens that are not
			// in the synchronization set.
			do {
				token = nextToken();
			} while (!(token instanceof EofToken)
					&& !syncSet.contains(token.getType()));
		}

		return token;
	}
}
