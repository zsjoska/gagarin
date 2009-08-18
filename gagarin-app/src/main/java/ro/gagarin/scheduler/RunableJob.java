package ro.gagarin.scheduler;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;

class RunableJob {

	private static final ManagerFactory FACTORY = BasicManagerFactory
			.getInstance();

	private final ScheduledJob job;

	private int toExecute;

	private long lastRun;

	public RunableJob(ScheduledJob job) {
		this.job = job;
		this.lastRun = (System.currentTimeMillis() + job.getInitialWait())
		- job.getPeriod();
		if (job.getPeriod() == 0) {
			// once to be executed
			this.toExecute = 1;
		} else {
			this.toExecute = job.getCount();
		}
	}

	public void run() {
		
		// TODO: move this session creation to the constructor and set the right timeout for the session
		Session session = createSession();
		
		System.out.println("toExecute:"+this.toExecute);
		AppLog log = FACTORY.getLogManager(session, BasicScheduleManager.class);
		try {
			log.debug("Executing job " + job.getName() + "#" + job.getId());
			job.execute(session, log);
			log.debug("Finished job " + job.getName() + "#" + job.getId());
		} catch (Exception e) {
			log.error("Exception executing job " + job.getName() + "#"
					+ job.getId(), e);
		}
		FACTORY.releaseSession(session);
		FACTORY.getSessionManager().destroySession(session);
	}

	private Session createSession() {
		SessionManager sessionManager = FACTORY.getSessionManager();
		Session session = sessionManager.createSession(null, "SCHEDULER",
				FACTORY);
		try {
			sessionManager.acquireSession(session.getSessionString());
		} catch (SessionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return session;
	}

	public long getNextRun() {
		// negative is infinite, positive is to be executed
		if (toExecute < 0 || toExecute > 0) {
			return this.lastRun + this.job.getPeriod();
		}
		return 0;
	}

	public Long getId() {
		return this.job.getId();
	}

	public void markExecuted() {
		this.lastRun += this.job.getPeriod();
		if (this.toExecute > 0)
			this.toExecute--;
	}

}
