package org.csovessoft.contabil.exceptions;

public class UserNotFoundException extends ExceptionBase {

	public UserNotFoundException() {
		super(ErrorCodes.USER_NOT_FOUND);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5953812143091133972L;

}
