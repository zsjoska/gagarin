package org.csovessoft.contabil;

import java.util.ArrayList;

import org.csovessoft.contabil.session.Session;

public interface SessionManager {

	Session createSession(String language, String reason);

	Session getSessionById(String sessionId);

	void logout(String id);

	ArrayList<Session> getExpiredSessions();

	void destroySession(Session session);

}
