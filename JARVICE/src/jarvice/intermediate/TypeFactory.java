package jarvice.intermediate;

import jarvice.intermediate.typeimpl.*;



public class TypeFactory {

	public static TypeSpec createType(TypeForm form){
		
		return new TypeSpecImpl(form);
		
	}
	
	
	public static TypeSpec createStringType(String value)
	{
		return new TypeSpecImpl(value);
	}
	
	
		
}
