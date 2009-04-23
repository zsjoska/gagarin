package org.csovessoft.contabil.session;

import java.util.ArrayList;

public interface SessionManager {

	Session createSession(String language, String reason);

	Session getSessionByID(String sessionID);

	void logout(String id);

	ArrayList<Session> getExpiredSessions();

	void destroySession(Session session);

}
