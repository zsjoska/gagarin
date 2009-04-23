package org.csovessoft.contabil.session;

import java.util.ArrayList;

import org.csovessoft.contabil.ModelFactory;
import org.csovessoft.contabil.SessionManager;
import org.csovessoft.contabil.SettingsChangeObserver;
import org.csovessoft.contabil.config.Config;

public class SessionCheckerThread extends Thread implements SettingsChangeObserver {

	private long SESSION_CHECK_PERIOD = ModelFactory.getConfigurationManager().getLong(
			Config.SESSION_CHECK_PERIOD);
	private volatile boolean terminate = false;
	private final SessionManager sessionManager;

	public SessionCheckerThread(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
		this.setDaemon(true);
		ModelFactory.getConfigurationManager().registerForChange(this);
	}

	@Override
	public void run() {
		try {
			while (!this.terminate) {
				synchronized (this) {
					this.wait(SESSION_CHECK_PERIOD);
					ArrayList<Session> expiredSessions = this.sessionManager.getExpiredSessions();
					for (Session session : expiredSessions) {
						this.sessionManager.destroySession(session);
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
