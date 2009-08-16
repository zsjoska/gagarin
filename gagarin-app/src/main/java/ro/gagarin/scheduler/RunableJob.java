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

	public RunableJob(ScheduledJob job) {
		this.job = job;
	}

	public void run() {
		Session session = createSession();

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
		// TODO Auto-generated method stub
		return 0;
	}

}
