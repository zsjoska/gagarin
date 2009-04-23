package org.csovessoft.contabil.exceptions;

public class UserAlreadyExistsException extends ExceptionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2216238959959727388L;
	private final String username;

	public UserAlreadyExistsException(String username) {
		super(ErrorCodes.USER_ALREADY_EXISTS);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

}
