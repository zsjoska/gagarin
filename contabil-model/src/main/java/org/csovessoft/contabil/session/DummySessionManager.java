package org.csovessoft.contabil.session;

public class DummySessionManager implements SessionManager {

	private static final DummySessionManager INSTANCE = new DummySessionManager();

	private DummySessionManager() {

	}

	public static SessionManager getInstance() {
		return INSTANCE;
	}

	@Override
	public Session storeNewSession(String username, String reason) {
		Session session = new Session();
		session.setId(String.valueOf(System.currentTimeMillis()));
		return session;
	}
}
