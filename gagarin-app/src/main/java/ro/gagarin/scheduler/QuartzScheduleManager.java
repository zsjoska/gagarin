package ro.gagarin.scheduler;

import ro.gagarin.ScheduleManager;

public class QuartzScheduleManager implements ScheduleManager {

	private static Scheduler defaultScheduler;

	static {
		defaultScheduler = new Scheduler();
		defaultScheduler.start();
	}

	@Override
	public long scheduleJob(ScheduledJob job) {
		return 0;
	}

}
