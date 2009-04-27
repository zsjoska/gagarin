package org.csovessoft.contabil.exceptions;

public class ExceptionBase extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5402532633664769260L;

	private final int errorCode;

	public ExceptionBase(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
