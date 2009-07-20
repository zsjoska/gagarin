package ro.gagarin;

import ro.gagarin.scheduler.ScheduledJob;

public interface ScheduleManager {

	long scheduleJob(ScheduledJob job);
}
