package ro.gagarin.scheduler;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.application.objects.AppUser;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;

class SessionJob extends SimpleJob {

    private static final transient Logger LOG = Logger.getLogger(SessionJob.class);
    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private Session session;

    public SessionJob(ScheduledJob job) {
	super(job);
	session = createSession();
	if (session.getSessionTimeout() < job.getPeriod() * 2)
	    session.setSessiontimeout(job.getPeriod() * 2);
    }

    public void run() {

	long start = System.currentTimeMillis();
	long delay = start - this.getNextRun();

	String msg = "Job #" + this.getId() + " execution delay:" + delay;
	if (delay > 2)
	    if (delay < 10) {
		LOG.debug(msg);
	    } else if (delay < 20) {
		LOG.warn(msg);
	    } else if (delay < 50) {
		LOG.info(msg);
	    } else if (delay >= 50) {
		LOG.error(msg);
	    }

	SessionManager sessionManager = FACTORY.getSessionManager();
	try {
	    sessionManager.acquireSession(session.getSessionString());
	} catch (SessionNotFoundException e) {
	    LOG.error("Session expired for job " + this.getJob());
	    super.markDone();
	    return;
	}

	AppLog log = FACTORY.getLogManager(session, SessionJob.class);
	try {
	    log.debug("Executing job " + getJob().getName() + "#" + getJob().getId());
	    getJob().execute(session, log);
	    log.debug("Finished job " + getJob().getName() + "#" + getJob().getId());
	} catch (Exception e) {
	    log.error("Exception executing job " + getJob().getName() + "#" + getJob().getId(), e);
	}
	FACTORY.releaseSession(session);

	long end = System.currentTimeMillis();
	if (end - start > this.getPeriod()) {
	    LOG.error("Job #" + this.getId() + " execution takes longer than the period:" + this.getPeriod()
		    + "; duration:" + (end - start));
	}
    }

    public void destroyJob() {
	FACTORY.getSessionManager().destroySession(session);
    }

    private Session createSession() {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.createSession(null, getJob().getName(), FACTORY);
	AppUser user = new AppUser();
	user.setUsername("SCHEDULER");
	session.setUser(user);
	return session;
    }
}
