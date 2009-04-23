package org.csovessoft.contabil.exceptions;

public class SessionNotFoundException extends ExceptionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 210431360198987678L;
	private final String sessionID;

	public SessionNotFoundException(String sessionID) {
		super(ErrorCodes.SESSION_NOT_FOUND);
		this.sessionID = sessionID;
	}

	public String getSessionID() {
		return sessionID;
	}

}
