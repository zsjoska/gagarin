package org.csovessoft.contabil.exceptions;

public class FieldRequiredException extends ExceptionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2594027123632142087L;
	private final String fieldname;

	public FieldRequiredException(String fieldname) {
		super(ErrorCodes.FIELD_REQUIRED);
		this.fieldname = fieldname;
	}

	public String getFieldname() {
		return fieldname;
	}

}
