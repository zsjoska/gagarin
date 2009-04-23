package org.csovessoft.contabil.session;

import java.util.ArrayList;

public class SessionCheckerThread extends Thread {

	private static final int SESSION_CHECK_PERIOD = 3000;
	private volatile boolean terminate = false;
	private final SessionManager sessionManager;

	public SessionCheckerThread(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
		this.setDaemon(true);
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

}
