package org.csovessoft.contabil.exceptions;

public class SessionNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 210431360198987678L;
	private final String sessionID;

	public SessionNotFoundException(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionID() {
		return sessionID;
	}

}
