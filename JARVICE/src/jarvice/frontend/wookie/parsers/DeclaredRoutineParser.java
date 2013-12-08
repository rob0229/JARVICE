package jarvice.frontend.wookie.parsers;

import java.util.ArrayList;
import java.util.EnumSet;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import jarvice.intermediate.symtabimpl.*;
import jarvice.intermediate.typeimpl.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.*;
import static jarvice.intermediate.symtabimpl.Predefined.charType;
import static jarvice.intermediate.symtabimpl.Predefined.integerType;
import static jarvice.intermediate.symtabimpl.RoutineCodeImpl.*;



public class DeclaredRoutineParser extends DeclarationsParser {
	
	public DeclaredRoutineParser(WookieParserTD parent) {
		super(parent);
	}

	private static int dummyCounter = 0; // counter for dummy routine names

	
	public SymTabEntry parse(Token token, SymTabEntry parentId)
			throws Exception {
	
		Token FunctionReturnToken = token;
	
		Definition routineDefn = null;
		String dummyName = null;
		SymTabEntry routineId = null;
		TokenType routineType = token.getType();

		// Initialize.
		switch ((WookieTokenType) routineType) {

			case INT: {
				// save the int data type for use later
				FunctionReturnToken = token;
				token = nextToken(); // consume Int			
				routineDefn = DefinitionImpl.FUNCTION;
				dummyName = "DummyProgramName".toLowerCase()+ String.format("%03d", ++dummyCounter);			
				break;
			}
			
			case CHAR: {
				// save the char data type for use later
				FunctionReturnToken = token;
				token = nextToken(); // consume Char
				routineDefn = DefinitionImpl.FUNCTION;
				dummyName = "DummyProgramName".toLowerCase()+ String.format("%03d", ++dummyCounter);
				break;
			}
			
			case VOID: {
				// save the void data type for use later
				FunctionReturnToken = token;
				token = nextToken(); // consume PROGRAM
				routineDefn = DefinitionImpl.PROCEDURE;
				dummyName = "DummyProgramName".toLowerCase()+ String.format("%03d", ++dummyCounter);
				break;
			}
	
			default: {
				routineDefn = DefinitionImpl.PROGRAM;
				dummyName = "DummyProgramName".toLowerCase();
				break;
			}
		}
		
		// Parse the routine name.
		routineId = parseRoutineName(token, dummyName);	
		
		routineId.setDefinition(routineDefn);
		token = currentToken();	
			
		    
		
		// Create new intermediate code for the routine.
		ICode iCode = ICodeFactory.createICode();
		routineId.setAttribute(ROUTINE_ICODE, iCode);
		routineId.setAttribute(ROUTINE_ROUTINES, new ArrayList<SymTabEntry>());
		
		
		
		routineId.setAttribute(ROUTINE_SYMTAB, symTabStack.push());
		
		//String name = token.getText();
		//((SymTabImpl)symTabStack.push()).setfuncName(name);
		//((SymTabImpl)symTabStack.push()).setisFunc(true);
		
		parseFormalParameters(token, routineId);
		token = currentToken();
		
		if(FunctionReturnToken.getType() == INT){
	    	   routineId.setTypeSpec(integerType);
	       }
	    else if(FunctionReturnToken.getType() == CHAR){
	    	   routineId.setTypeSpec(charType);
	       }
	
		// Parse the routine's block  declaration.
		if (token.getType() == LEFT_BRACE){

			routineId.setAttribute(ROUTINE_CODE, DECLARED);
			BlockParser blockParser = new BlockParser(this);
			
			ICodeNode rootNode = blockParser.parse(token, routineId);
			iCode.setRoot(rootNode);
		}
		ArrayList<SymTabEntry> subroutines = (ArrayList<SymTabEntry>) parentId.getAttribute(ROUTINE_ROUTINES);
		subroutines.add(routineId);
		// Pop the routine's symbol table off the stack.
		
		symTabStack.pop();

		return routineId;
	}

	private SymTabEntry parseRoutineName(Token token, String dummyName)
			throws Exception {
		SymTabEntry routineId = null;

		// Parse the routine name identifier.
		if (token.getType() == IDENTIFIER) {
			String routineName = token.getText().toLowerCase();
			routineId = symTabStack.lookupLocal(routineName);

			// Not already defined locally: Enter into the local symbol table.
			if (routineId == null) {
				
				routineId = symTabStack.enterLocal(routineName);
				routineId.appendLineNumber(token.getLineNumber());
			}

			// If already defined, it should be a forward definition.
			else {
				routineId = null;
				errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
			}

			token = nextToken(); // consume routine name identifier
			
		}
		else 
		{

			errorHandler.flag(token, MISSING_IDENTIFIER, this);
		}

		// If necessary, create a dummy routine name symbol table entry.
		if (routineId == null) {
			routineId = symTabStack.enterLocal(dummyName);
		}
		
		return routineId;
	}

	
	private void parseHeader(Token token, SymTabEntry routineId)
			throws Exception {

		// Parse the routine's formal parameters.

		// If this is a function, parse and set its return type.
		if (routineId.getDefinition() == DefinitionImpl.FUNCTION) {
			VariableDeclarationsParser variableDeclarationsParser = new VariableDeclarationsParser(
					this);
			variableDeclarationsParser.setDefinition(DefinitionImpl.FUNCTION);
			TypeSpec type = variableDeclarationsParser.parseTypeSpec(token);

			token = currentToken();

			// The return type cannot be an array or record.
			if (type != null) {
				TypeForm form = type.getForm();
				if ((form == TypeFormImpl.ARRAY)
						|| (form == TypeFormImpl.RECORD)) {
					errorHandler.flag(token, INVALID_TYPE, this);
				}
			}

			// Missing return type.
			else {
				type = Predefined.undefinedType;
			}

			routineId.setTypeSpec(type);
			token = currentToken();
		}
	}

	// Synchronization set for a formal parameter sublist.
	private static final EnumSet<WookieTokenType> PARAMETER_SET = EnumSet.of(INT, CHAR, RIGHT_PAREN);
			

	// Synchronization set for the opening left parenthesis.
	private static final EnumSet<WookieTokenType> LEFT_PAREN_SET = EnumSet.of(LEFT_PAREN, LEFT_BRACE);
	

	// Synchronization set for the closing right parenthesis.
	private static final EnumSet<WookieTokenType> RIGHT_PAREN_SET = EnumSet.of(RIGHT_PAREN);

	/**
	 * Parse a routine's formal parameter list.
	 * 
	 * @param token
	 *            the current token.
	 * @param routineId
	 *            the symbol table entry of the declared routine's name.
	 * @throws Exception
	 *             if an error occurred.
	 */
	protected void parseFormalParameters(Token token, SymTabEntry routineId)
			throws Exception {
		// Parse the formal parameters if there is an opening left parenthesis.
		token = synchronize(LEFT_PAREN_SET);
		if (token.getType() == LEFT_PAREN) {
			token = nextToken(); // consume (
			TokenType tokenType1 = token.getType();
		
			
			ArrayList<SymTabEntry> parms = new ArrayList<SymTabEntry>();
			while(tokenType1 != RIGHT_PAREN ){
				
				parms.addAll(parseParmSublist(token, routineId));
				
				token = currentToken();

				tokenType1 = token.getType();
			}

			//This sync needs to check for Int or Char
			token = synchronize(PARAMETER_SET);
	
			// Closing right parenthesis.
			if (token.getType() == RIGHT_PAREN) {
				token = nextToken(); // consume )
			} else {
				errorHandler.flag(token, MISSING_RIGHT_PAREN, this);
			}

			routineId.setAttribute(ROUTINE_PARMS, parms);
		}
	}

	// Synchronization set to follow a formal parameter identifier.
	private static final EnumSet<WookieTokenType> PARAMETER_FOLLOW_SET = EnumSet
			.of(COLON, RIGHT_PAREN, SEMICOLON);
	static {
		PARAMETER_FOLLOW_SET.addAll(DeclarationsParser.DECLARATION_START_SET);
	}

	// Synchronization set for the , token.
	private static final EnumSet<WookieTokenType> COMMA_SET = EnumSet.of(COMMA,
			COLON, IDENTIFIER, RIGHT_PAREN, SEMICOLON);
	static {
		COMMA_SET.addAll(DeclarationsParser.DECLARATION_START_SET);
	}

	/**
	 * Parse a sublist of formal parameter declarations.
	 * 
	 * @param token
	 *            the current token.
	 * @param routineId
	 *            the symbol table entry of the declared routine's name.
	 * @return the sublist of symbol table entries for the parm identifiers.
	 * @throws Exception
	 *             if an error occurred.
	 */
	private ArrayList<SymTabEntry> parseParmSublist(Token token,
			SymTabEntry routineId) throws Exception {
	
		//name = main, definition == function
		
		
		Definition parmDefn = null;
		TokenType tokenType = token.getType();

		parmDefn = VALUE_PARM;
		
		SymTabEntry sublist = null;
		
		
		
		if(tokenType == INT){
			IntDeclarationsParser intDeclarationsParser = new IntDeclarationsParser(this);
			intDeclarationsParser.setDefinition(parmDefn);
			token = nextToken();
			sublist = intDeclarationsParser.parseIdentifier(token);
			sublist.setTypeSpec(integerType);
			//set typeForm value here?
		}
		else if (tokenType == CHAR){
			
			IntDeclarationsParser intDeclarationsParser = new IntDeclarationsParser(this);
			intDeclarationsParser.setDefinition(parmDefn);
			token = nextToken();
			sublist = intDeclarationsParser.parseIdentifier(token);
			sublist.setTypeSpec(charType);
		}
		token = nextToken();
		token = currentToken();
        if (token.getType() == COMMA){
        	token = nextToken();
        }
        
		tokenType = token.getType();

			token = synchronize(PARAMETER_SET);
			ArrayList<SymTabEntry> SC = new ArrayList<SymTabEntry>();
			SC.add(sublist);
		return SC;
	}
}
