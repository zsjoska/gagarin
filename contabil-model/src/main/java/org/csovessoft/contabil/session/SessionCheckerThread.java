package org.csovessoft.contabil.session;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.csovessoft.contabil.ModelFactory;
import org.csovessoft.contabil.SessionManager;
import org.csovessoft.contabil.config.Config;
import org.csovessoft.contabil.config.SettingsChangeObserver;

public class SessionCheckerThread extends Thread implements SettingsChangeObserver {

	private static final transient Logger LOG = Logger.getLogger(SessionCheckerThread.class);

	private long SESSION_CHECK_PERIOD = ModelFactory.getConfigurationManager().getLong(
			Config.SESSION_CHECK_PERIOD);
	private volatile boolean terminate = false;
	private final SessionManager sessionManager;

	public SessionCheckerThread(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
		this.setDaemon(true);
		ModelFactory.getConfigurationManager().registerForChange(this);
		LOG.info("Session checker initialized with " + SESSION_CHECK_PERIOD + "ms period");
	}

	@Override
	public void run() {
		try {
			while (!this.terminate) {
				synchronized (this) {
					this.wait(SESSION_CHECK_PERIOD);
					LOG.debug("SessionCheck looking for expired sessions");
					ArrayList<Session> expiredSessions = this.sessionManager.getExpiredSessions();
					LOG.debug("SessionCheck found" + expiredSessions.size() + " expired sessions");
					for (Session session : expiredSessions) {
						LOG.debug("Terminating expired session " + session.getId());
						this.sessionManager.destroySession(session);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("The session checker thread was interrupted", e);
		}
	}

	public void terminate() {
		this.terminate = true;
	}

	@Override
	public void configChanged(Config config, String value) {
		switch (config) {
		case SESSION_CHECK_PERIOD:
			SESSION_CHECK_PERIOD = Long.valueOf(value);
			break;
		}
	}

}
