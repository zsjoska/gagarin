package org.csovessoft.contabil.session;

import java.util.ArrayList;
import java.util.HashMap;

public class DummySessionManager implements SessionManager {

	private static final DummySessionManager INSTANCE = new DummySessionManager();

	private final HashMap<String, Session> sessions = new HashMap<String, Session>();

	private SessionCheckerThread chkSession = new SessionCheckerThread(INSTANCE);

	private DummySessionManager() {
		chkSession.start();
	}

	public static SessionManager getInstance() {
		return INSTANCE;
	}

	@Override
	public Session createSession(String language, String reason) {
		Session session = new Session();
		session.setId(String.valueOf(System.currentTimeMillis()));
		session.setLanguage(language);
		session.setReason(reason);
		session.setExpires(System.currentTimeMillis() + session.getSessionTimeout());
		this.sessions.put(session.getId(), session);
		return session;
	}

	@Override
	public Session getSessionByID(String sessionID) {
		Session session = this.sessions.get(sessionID);
		if (session.isExpired())
			return null;
		session.setExpires(System.currentTimeMillis() + session.getSessionTimeout());
		return session;
	}

	@Override
	public void logout(String id) {
		this.sessions.remove(id);
	}

	@Override
	public ArrayList<Session> getExpiredSessions() {
		ArrayList<Session> expiredSessions = new ArrayList<Session>();
		for (Session session : this.sessions.values()) {
			if (session.isExpired()) {
				expiredSessions.add(session);
			}
		}
		return expiredSessions;
	}

	@Override
	public void destroySession(Session session) {
		this.sessions.remove(session.getId());
	}
}
