package ro.gagarin.scheduler;

import org.apache.log4j.Logger;

import ro.gagarin.ScheduleManager;

public class DefaultScheduleManager implements ScheduleManager {

    private static final transient Logger LOG = Logger.getLogger(DefaultScheduleManager.class);
    private Scheduler defaultScheduler;

    @Override
    public long scheduleJob(ScheduledJob job, boolean createSession) {
	if (defaultScheduler == null) {
	    throw new NullPointerException("Scheduler not initialized");
	}
	job.setId(ScheduledJob.getNextId());
	LOG.info("Schedule Job:" + job.toString());
	return defaultScheduler.scheduleJob(job, createSession);
    }

    @Override
    public void updateJobRate(Long id, Long rate) {
	if (defaultScheduler == null) {
	    throw new NullPointerException("Scheduler not initialized");
	}
	LOG.info("Update job rate:" + id + " rate:" + rate);
	defaultScheduler.updateJobRate(id, rate);
    }

    @Override
    public void triggerExecution(ScheduledJob job) {
	if (defaultScheduler == null) {
	    throw new NullPointerException("Scheduler not initialized");
	}
	LOG.info("Trigger job execution:" + job.getId());
	defaultScheduler.triggerExecution(job.getId());
    }

    @Override
    public void initializeManager() {
	defaultScheduler = new Scheduler();
	defaultScheduler.start();
    }
}
