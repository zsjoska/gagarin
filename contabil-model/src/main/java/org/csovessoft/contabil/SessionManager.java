package org.csovessoft.contabil;

import java.util.ArrayList;

import org.csovessoft.contabil.session.Session;

/**
 * Base interface for session management. Provides methods for session creation,
 * deletion retrieval, etc.<br>
 * 
 * @author zsjoska
 * 
 */
public interface SessionManager {

	/**
	 * Creates a new, empty session with the specified language and scope. The
	 * implementation must call associateUser before use of the session.
	 * 
	 * @param language
	 *            the language code for error messages
	 * @param reason
	 *            the scope of the session
	 * @return a new initialized session
	 */
	Session createSession(String language, String reason);

	// TODO: create associateUser

	/**
	 * Returns the session identified by the given session ID.
	 * 
	 * @param sessionId
	 *            the ID of the requested session
	 * @return the session or <code>null</code> if the session was not found or
	 *         it was expired
	 */
	Session getSessionById(long sessionId);

	/**
	 * Destroys the session.
	 * 
	 * @param id
	 *            the ID of the session to be destroyed
	 */
	void logout(long id);

	/**
	 * Returns a list with sessions that expired. These sessions will be
	 * destroyed by the session manager's timer.
	 * 
	 * @return a list of expired sessions
	 */
	ArrayList<Session> getExpiredSessions();

	/**
	 * Destroys a session.
	 * 
	 * @param session
	 *            the session to be destroyed
	 */
	void destroySession(Session session);

}
