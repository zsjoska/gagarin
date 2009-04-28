package ro.gagarin.session;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import ro.gagarin.ConfigurationManager;
import ro.gagarin.ModelFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.config.Config;
import ro.gagarin.config.SettingsChangeObserver;

public class BasicSessionManager implements SessionManager, SettingsChangeObserver {

	private static final transient Logger LOG = Logger.getLogger(BasicSessionManager.class);

	private static final BasicSessionManager INSTANCE = new BasicSessionManager();

	private long USER_SESSION_TIMEOUT;

	private final HashMap<String, Session> sessions = new HashMap<String, Session>();

	private SessionCheckerThread chkSession = null;

	private long SESSION_CHECK_PERIOD;

	private BasicSessionManager() {
		LOG.debug("Creating BasicSessionManager");
		ConfigurationManager cfgManager = ModelFactory.getConfigurationManager(this);
		cfgManager.registerForChange(this);
		USER_SESSION_TIMEOUT = cfgManager.getLong(Config.USER_SESSION_TIMEOUT);
		SESSION_CHECK_PERIOD = cfgManager.getLong(Config.SESSION_CHECK_PERIOD);
		chkSession = new SessionCheckerThread(this);
		chkSession.start();
	}

	public static SessionManager getInstance() {
		return INSTANCE;
	}

	@Override
	public Session createSession(String language, String reason) {
		Session session = new Session();
		session.setSessiontimeout(USER_SESSION_TIMEOUT);
		session.setLanguage(language);
		session.setReason(reason);
		session.setExpires(System.currentTimeMillis() + session.getSessionTimeout());
		session.setSessionString(System.currentTimeMillis() + "-" + System.nanoTime());
		this.sessions.put(session.getSessionString(), session);
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
		this.sessions.remove(session.getSessionString());
	}

	@Override
	public boolean configChanged(Config config, String value) {
		switch (config) {
		case USER_SESSION_TIMEOUT:
			USER_SESSION_TIMEOUT = Long.valueOf(value);
			return true;
		case SESSION_CHECK_PERIOD:
			SESSION_CHECK_PERIOD = Long.valueOf(value);
			return true;
		}
		return false;
	}

	public long getSessionCheckPeriod() {
		return SESSION_CHECK_PERIOD;
	}

	@Override
	public void release() {
		// TODO: iterate remaining sessions and warn if an active session was
		// left
	}
}
