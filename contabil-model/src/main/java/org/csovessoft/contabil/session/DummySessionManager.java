package org.csovessoft.contabil.session;

import java.util.HashMap;

import org.csovessoft.contabil.exceptions.SessionNotFoundException;

public class DummySessionManager implements SessionManager {

	private static final DummySessionManager INSTANCE = new DummySessionManager();

	private final HashMap<String, Session> sessions = new HashMap<String, Session>();

	private DummySessionManager() {

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
		this.sessions.put(session.getId(), session);
		return session;
	}

	@Override
	public Session getSessionByID(String sessionID)
			throws SessionNotFoundException {
		Session session = this.sessions.get(sessionID);
		if (session == null)
			throw new SessionNotFoundException(sessionID);
		return session;
	}

	@Override
	public void logout(String id) {
		this.sessions.remove(id);
	}
}
