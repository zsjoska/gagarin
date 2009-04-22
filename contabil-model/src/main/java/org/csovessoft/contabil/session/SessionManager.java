package org.csovessoft.contabil.session;

import org.csovessoft.contabil.exceptions.SessionNotFoundException;

public interface SessionManager {

	Session createSession(String language, String reason);

	Session getSessionByID(String sessionID) throws SessionNotFoundException;

	void logout(String id);

}
