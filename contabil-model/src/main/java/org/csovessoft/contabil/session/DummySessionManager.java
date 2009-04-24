package org.csovessoft.contabil.session;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.csovessoft.contabil.ModelFactory;
import org.csovessoft.contabil.SessionManager;
import org.csovessoft.contabil.config.Config;
import org.csovessoft.contabil.config.SettingsChangeObserver;

public class DummySessionManager implements SessionManager, SettingsChangeObserver {

	private static final transient Logger LOG = Logger.getLogger(DummySessionManager.class);

	private static final DummySessionManager INSTANCE = new DummySessionManager();

	private long USER_SESSION_TIMEOUT = ModelFactory.getConfigurationManager().getLong(
			Config.USER_SESSION_TIMEOUT);

	private final HashMap<String, Session> sessions = new HashMap<String, Session>();

	private SessionCheckerThread chkSession = new SessionCheckerThread(INSTANCE);

	private DummySessionManager() {
		ModelFactory.getConfigurationManager().registerForChange(this);
		chkSession.start();
	}

	public static SessionManager getInstance() {
		return INSTANCE;
	}

	@Override
	public Session createSession(String language, String reason) {
		Session session = new Session(USER_SESSION_TIMEOUT);
		session.setId(System.currentTimeMillis() + "-" + System.nanoTime());
		session.setLanguage(language);
		session.setReason(reason);
		session.setExpires(System.currentTimeMillis() + session.getSessionTimeout());
		this.sessions.put(session.getId(), session);
		LOG.info("Created Session " + session.getId());
		return session;
	}

	@Override
	public Session getSessionById(String sessionId) {
		Session session = this.sessions.get(sessionId);

		if (session == null) {
			LOG.debug("The requested session was not found:" + sessionId);
			return null;
		}

		if (session.isExpired()) {
			LOG.debug("The requested session expired:" + sessionId);
			return null;
		}

		session.setExpires(System.currentTimeMillis() + session.getSessionTimeout());
		return session;
	}

	@Override
	public void logout(String id) {
		Session session = this.sessions.get(id);
		if (session != null)
			destroySession(session);
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
		LOG.info("Destroy session " + session.getId());
		this.sessions.remove(session.getId());
	}

	@Override
	public void configChanged(Config config, String value) {
		switch (config) {
		case USER_SESSION_TIMEOUT:
			USER_SESSION_TIMEOUT = Long.valueOf(value);
			break;
		}
	}
}
