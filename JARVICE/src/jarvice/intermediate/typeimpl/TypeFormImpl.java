package jarvice.intermediate.typeimpl;

import jarvice.intermediate.TypeForm;

public enum TypeFormImpl implements TypeForm {
	SCALAR, ENUMERATION, SUBRANGE, ARRAY, RECORD;
	
		public String toString()
		{
			return super.toString().toLowerCase();
		}

}