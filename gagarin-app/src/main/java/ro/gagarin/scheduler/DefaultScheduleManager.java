package ro.gagarin.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.manager.ScheduleManager;
import ro.gagarin.session.Session;

public class DefaultScheduleManager implements ScheduleManager {

    private static final transient Logger LOG = Logger.getLogger(DefaultScheduleManager.class);
    private Scheduler defaultScheduler = null;
    private List<ScheduledJob> earlyJobs = new ArrayList<ScheduledJob>();
    private static ManagerFactory FACTORY = BasicManagerFactory.getInstance();

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
    public long scheduleJob(ScheduledJob job, Session session) {
	job.setId(ScheduledJob.getNextId());
	LOG.info("Schedule Job:" + job.toString());

	if (defaultScheduler == null) {
	    earlyJobs.add(job);
	    return job.getId();
	}

	Session clone = FACTORY.getSessionManager().cloneSession(session);

	return defaultScheduler.scheduleJob(job, session);
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

    @Override
    public List<JobController> exportJobs() {
	if (defaultScheduler != null) {
	    return defaultScheduler.exportJobs();
	} else {
	    ArrayList<JobController> jobs = new ArrayList<JobController>();
	    return jobs;
	}
    }

    @Override
    public List<SchedulerThread> exportThreads() {
	if (defaultScheduler != null) {
	    return defaultScheduler.exportThreads();
	} else {
	    ArrayList<SchedulerThread> threads = new ArrayList<SchedulerThread>();
	    return threads;
	}
    }
}
