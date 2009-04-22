package org.csovessoft.contabil.exceptions;

public class SessionNotFoundException extends Exception {

	private final String sessionID;

	public SessionNotFoundException(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionID() {
		return sessionID;
	}

}
