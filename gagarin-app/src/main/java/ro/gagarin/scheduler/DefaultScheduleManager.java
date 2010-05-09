package ro.gagarin.scheduler;

import org.apache.log4j.Logger;

import ro.gagarin.ScheduleManager;

public class DefaultScheduleManager implements ScheduleManager {

    private static final transient Logger LOG = Logger.getLogger(DefaultScheduleManager.class);
    private static Scheduler defaultScheduler;

    static {
	defaultScheduler = new Scheduler();
	defaultScheduler.start();
    }

    @Override
    public long scheduleJob(ScheduledJob job, boolean createSession) {
	job.setId(ScheduledJob.getNextId());
	LOG.info("Schedule Job:" + job.toString());
	return defaultScheduler.scheduleJob(job, createSession);
    }

    @Override
    public void updateJobRate(Long id, Long rate) {
	LOG.info("Update job rate:" + id + " rate:" + rate);
	defaultScheduler.updateJobRate(id, rate);
    }

    @Override
    public void triggerExecution(ScheduledJob job) {
	LOG.info("Trigger job execution:" + job.getId());
	defaultScheduler.triggerExecution(job.getId());
    }

    @Override
    public void initializeManager() {
	// TODO move here the init stuff
    }
}
