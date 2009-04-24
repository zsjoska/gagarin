package org.csovessoft.contabil.exceptions;

import org.apache.log4j.Logger;

public class UserAlreadyExistsException extends ExceptionBase {

	private static final transient Logger LOG = Logger.getLogger(UserNotFoundException.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2216238959959727388L;
	private final String value;

	private final String field;

	public UserAlreadyExistsException(String field, String value) {
		super(ErrorCodes.USER_ALREADY_EXISTS);
		this.field = field;
		this.value = value;
		LOG.error("User already exists with this field " + field + "=" + value);
	}

	public String getUsername() {
		return value;
	}

	public String getField() {
		return field;
	}

}
