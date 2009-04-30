package ro.gagarin.exceptions;

import org.apache.log4j.Logger;

import ro.gagarin.session.Session;

public class SessionNotFoundException extends ExceptionBase {

	private static final transient Logger LOG = Logger.getLogger(UserNotFoundException.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 210431360198987678L;
	private final String sessionID;

	public SessionNotFoundException(String sessionID) {
		super(ErrorCodes.SESSION_NOT_FOUND);
		this.sessionID = sessionID;
		LOG.error("Session was not found: " + sessionID);
	}

	public SessionNotFoundException(Session session) {
		super(ErrorCodes.SESSION_NOT_FOUND);
		this.sessionID = session.getSessionString();
		// TODO: construct with more details
	}

	public String getSessionID() {
		return sessionID;
	}

}
