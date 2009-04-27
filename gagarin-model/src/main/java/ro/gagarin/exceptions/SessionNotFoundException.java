package ro.gagarin.exceptions;

import org.apache.log4j.Logger;

public class SessionNotFoundException extends ExceptionBase {

	private static final transient Logger LOG = Logger.getLogger(UserNotFoundException.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 210431360198987678L;
	private final long sessionID;

	public SessionNotFoundException(long sessionID) {
		super(ErrorCodes.SESSION_NOT_FOUND);
		this.sessionID = sessionID;
		LOG.error("Session was not found: " + sessionID);
	}

	public long getSessionID() {
		return sessionID;
	}

}
