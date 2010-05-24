package ro.gagarin.session;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ro.gagarin.manager.SessionManager;

// TODO:(1) move this to the application module
public class SessionCheckerThread extends Thread {

    private static final transient Logger LOG = Logger.getLogger(SessionCheckerThread.class);

    private long SESSION_CHECK_PERIOD;
    private volatile boolean terminate = false;
    private final SessionManager sessionManager;

    public SessionCheckerThread(SessionManager sessionManager) {
	this.sessionManager = sessionManager;
	this.setDaemon(true);
	SESSION_CHECK_PERIOD = sessionManager.getSessionCheckPeriod();
	LOG.info("Session checker initialized with " + SESSION_CHECK_PERIOD + "ms period");
    }

    @Override
    public void run() {
	try {
	    while (!this.terminate) {
		synchronized (this) {
		    this.wait(sessionManager.getSessionCheckPeriod());
		    ArrayList<Session> expiredSessions = this.sessionManager.getExpiredSessions();
		    if (expiredSessions == null || expiredSessions.size() == 0)
			continue;
		    LOG.debug("SessionCheck found " + expiredSessions.size() + " expired sessions");
		    for (Session session : expiredSessions) {
			LOG.debug("Terminating expired session " + session.getSessionString());
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

}
