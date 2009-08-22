package ro.gagarin.scheduler;

import ro.gagarin.ScheduleManager;

public class DefaultScheduleManager implements ScheduleManager {

	private static Scheduler defaultScheduler;

	static {
		defaultScheduler = new Scheduler();
		defaultScheduler.start();
	}

	@Override
	public long scheduleJob(ScheduledJob job) {
		return defaultScheduler.scheduleJob(job);
	}

	@Override
	public void updateJobRate(Long id, Long rate) {
		defaultScheduler.updateJobRate(id, rate);
	}

	@Override
	public void triggerExecution(ScheduledJob configImportJob) {
		defaultScheduler.triggerExecution(configImportJob.getId());
	}

}
