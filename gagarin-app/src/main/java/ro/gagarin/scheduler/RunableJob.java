package ro.gagarin.scheduler;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.application.objects.AppUser;
import ro.gagarin.config.DBConfigManager;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;

class RunableJob {

	private static final transient Logger LOG = Logger
			.getLogger(RunableJob.class);
	private static final ManagerFactory FACTORY = BasicManagerFactory
			.getInstance();

	private final ScheduledJob job;

	private int toExecute;

	private long lastRun;

	private long period;
	private Session session;

	public RunableJob(ScheduledJob job) {
		this.job = job;
		this.period = job.getPeriod();
		this.lastRun = (System.currentTimeMillis() + job.getInitialWait())
				- job.getPeriod();
		if (job.getPeriod() == 0) {
			// once to be executed
			this.toExecute = 1;
		} else {
			this.toExecute = job.getCount();
		}
		// TODO: move this session creation to the constructor and set the right
		// timeout for the session
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
			LOG.error("Session expired for job " + this.job);
			return;
		}

		AppLog log = FACTORY.getLogManager(session, RunableJob.class);
		try {
			log.debug("Executing job " + job.getName() + "#" + job.getId());
			job.execute(session, log);
			log.debug("Finished job " + job.getName() + "#" + job.getId());
		} catch (Exception e) {
			log.error("Exception executing job " + job.getName() + "#"
					+ job.getId(), e);
		}
		FACTORY.releaseSession(session);

		// TODO: destroy the session for the last run
		// FACTORY.getSessionManager().destroySession(session);

		long end = System.currentTimeMillis();
		if (end - start > this.getPeriod()) {
			LOG.error("Job #" + this.getId()
					+ " execution takes longer than the period:"
					+ this.getPeriod() + "; duration:" + (end - start));
		}
	}

	private Session createSession() {
		SessionManager sessionManager = FACTORY.getSessionManager();
		Session session = sessionManager.createSession(null, job.getName(),
				FACTORY);
		AppUser user = new AppUser();
		user.setUsername("SCHEDULER");
		session.setUser(user);
		return session;
	}

	public long getNextRun() {
		// negative is infinite, positive is to be executed
		if (toExecute < 0 || toExecute > 0) {
			return this.lastRun + this.period;
		}
		return 0;
	}

	public Long getId() {
		return this.job.getId();
	}

	public void markExecuted() {
		this.lastRun += this.period;
		if (this.toExecute > 0)
			this.toExecute--;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public long getPeriod() {
		return period;
	}

	public void markToExecuteNow() {
		this.lastRun = System.currentTimeMillis() - period;
	}
}
