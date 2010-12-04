package ro.gagarin.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ro.gagarin.manager.ScheduleManager;

public class DefaultScheduleManager implements ScheduleManager {

    private static final transient Logger LOG = Logger.getLogger(DefaultScheduleManager.class);
    private Scheduler defaultScheduler = null;
    private List<ScheduledJob> earlyJobs = new ArrayList<ScheduledJob>();

    @Override
    public long scheduleJob(ScheduledJob job, boolean createSession) {
	job.setId(ScheduledJob.getNextId());
	LOG.info("Schedule Job:" + job.toString());

	if (defaultScheduler == null) {
	    earlyJobs.add(job);
	    return job.getId();
	}

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
	for (ScheduledJob job : this.earlyJobs) {
	    defaultScheduler.scheduleJob(job, false);
	}
	this.earlyJobs.clear();
	this.earlyJobs = null;
    }
}
