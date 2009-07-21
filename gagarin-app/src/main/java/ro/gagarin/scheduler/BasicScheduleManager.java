package ro.gagarin.scheduler;

import java.util.Timer;
import java.util.TimerTask;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.ScheduleManager;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;

public class BasicScheduleManager implements ScheduleManager {

	private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

	private static Timer timer = new Timer("TIMER", true);

	private static class RunableJob extends TimerTask {

		private final ScheduledJob job;

		public RunableJob(ScheduledJob job) {
			this.job = job;
		}

		@Override
		public void run() {
			Session session = createSession();
			AppLog log = FACTORY.getLogManager(session, BasicScheduleManager.class);
			try {
				log.debug("Executing job " + job.getName() + "#" + job.getId());
				job.execute(session);
				log.debug("Finished job " + job.getName() + "#" + job.getId());
			} catch (Exception e) {
				log.error("Exception executing job " + job.getName() + "#" + job.getId(), e);
			}
			FACTORY.releaseSession(session);
			FACTORY.getSessionManager().destroySession(session);
		}

		private Session createSession() {
			Session session = FACTORY.getSessionManager().createSession(null, "SCHEDULER", FACTORY);
			return session;
		}

	}

	@Override
	public long scheduleJob(ScheduledJob job) {
		if (job.getPeriod() == 0) {
			timer.schedule(new RunableJob(job), job.getInitialWait());
			return job.getId();
		}
		timer.schedule(new RunableJob(job), job.getInitialWait(), job.getPeriod());
		return job.getId();
	}
}
