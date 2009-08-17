package ro.gagarin.scheduler;

import java.util.Timer;
import java.util.TimerTask;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.ScheduleManager;

@Deprecated
public class BasicScheduleManager implements ScheduleManager {

	// TODO: implement a stronger scheduler which support later change on
	// execution period

	private static final ManagerFactory FACTORY = BasicManagerFactory
			.getInstance();

	private static Timer timer = new Timer("TIMER", true);


	@Override
	public long scheduleJob(final ScheduledJob job) {

		final RunableJob rJob = new RunableJob(job);
		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				rJob.run();
			}
		};

		job.setId(ScheduledJob.getNextId());

		if (job.getPeriod() == 0) {
			timer.schedule(tt, job.getInitialWait());
			return job.getId();
		}
		timer.schedule(tt, job.getInitialWait(), job.getPeriod());
		return job.getId();
	}


	@Override
	public void updateJobRate(Long id, Long rate) {
		throw new RuntimeException("this is not supported");
	}
}
