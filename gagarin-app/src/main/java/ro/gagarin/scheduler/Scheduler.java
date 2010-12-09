package ro.gagarin.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.config.Configuration;
import ro.gagarin.config.SettingsChangeObserver;

public class Scheduler implements SettingsChangeObserver {

    private static final transient Logger LOG = Logger.getLogger(Scheduler.class);
    private ArrayList<SchedulerThreadImpl> threads = new ArrayList<SchedulerThreadImpl>();
    private int threadCount;
    private HashMap<Long, SimpleJob> pendingJobStore = new HashMap<Long, SimpleJob>();
    private HashMap<Long, SimpleJob> allJobStore = new HashMap<Long, SimpleJob>();
    private AtomicInteger threadId = new AtomicInteger();

    public Scheduler() {
	this(Configuration.SCHEDULER_THREADS);
	BasicManagerFactory.getInstance().getConfigurationManager().registerForChange("SCHEDULER_THREADS", this);
    }

    public Scheduler(int threadCount) {
	this.threadCount = threadCount;
    }

    public void start() {
	createNewThreads(this.threadCount);
    }

    public void createNewThreads(int count) {
	for (int i = 0; i < count; i++) {
	    SchedulerThreadImpl thread = new SchedulerThreadImpl(this, threadId.getAndIncrement());
	    thread.start();
	    threads.add(thread);
	}
    }

    public boolean shutdown() {
	return false;
    }

    private SimpleJob getNextJob() {
	long minNextRun = Long.MAX_VALUE;
	SimpleJob nextJob = null;
	for (SimpleJob job : this.pendingJobStore.values()) {
	    long nextRun = job.getNextRun();
	    if (nextRun < minNextRun) {
		minNextRun = nextRun;
		nextJob = job;
	    }
	}
	return nextJob;
    }

    public void releaseJob(SimpleJob nextJob) {
	synchronized (this) {
	    nextJob.markExecuted();
	    this.pendingJobStore.put(nextJob.getId(), nextJob);
	    LOG.debug("Job #" + nextJob.getId() + " next execution is planned at " + nextJob.getNextRun());
	    this.notify();
	}
    }

    public SimpleJob waitNextJob() throws InterruptedException {
	SimpleJob nextJob = null;
	long toWait = Long.MAX_VALUE;
	synchronized (this) {

	    nextJob = this.getNextJob();
	    if (nextJob == null) {
		// no jobs to execute
		this.wait();
		return null;
	    }

	    long nextRun = nextJob.getNextRun();

	    if (nextRun == 0) {
		// this job has no left execution; remove it and forget it
		this.pendingJobStore.remove(nextJob.getId());
		this.allJobStore.remove(nextJob.getId());
		// we will destroy the job outside of the synchronized block
	    } else {

		toWait = nextRun - System.currentTimeMillis();
		if (toWait < 10) {
		    this.pendingJobStore.remove(nextJob.getId());
		} else {
		    this.wait(toWait);
		    return null;
		}
	    }
	}

	if (nextJob != null && nextJob.getNextRun() == 0) {
	    // this was removed already from the stores
	    nextJob.destroyJob();
	}

	if (toWait < 10) {
	    toWait = nextJob.getNextRun() - System.currentTimeMillis();
	    if (toWait > 0) {
		Thread.sleep(toWait);
	    }
	    return nextJob;
	}

	return null;
    }

    public long scheduleJob(ScheduledJob job, boolean createDBConnecton) {
	if (job.getId() == null) {
	    job.setId(ScheduledJob.getNextId());
	}
	synchronized (this) {
	    SimpleJob runableJob = null;
	    if (createDBConnecton) {
		runableJob = new SessionJob(job);
	    } else {
		runableJob = new SimpleJob(job);
	    }
	    this.pendingJobStore.put(job.getId(), runableJob);
	    this.allJobStore.put(job.getId(), runableJob);
	    this.notify();
	}
	return job.getId();
    }

    public synchronized void updateJobRate(Long id, Long rate) {
	SimpleJob job = this.allJobStore.get(id);
	if (job != null) {
	    job.setPeriod(rate);
	    this.notify();
	} else
	    LOG.error("Job was not found in job store:" + id);
    }

    public synchronized void triggerExecution(Long id) {
	SimpleJob job = this.allJobStore.get(id);
	if (job != null) {
	    job.markToExecuteNow();
	    this.notify();
	} else
	    LOG.error("Job was not found in job store:" + id);
    }

    @Override
    public boolean configChanged(String config, String value) {
	if (Configuration.SCHEDULER_THREADS.equals(this.threadCount)) {
	    return true;
	}
	if (Configuration.SCHEDULER_THREADS > threadCount) {
	    synchronized (this) {
		int delta = Configuration.SCHEDULER_THREADS - threadCount;
		createNewThreads(delta);
		this.threadCount = Configuration.SCHEDULER_THREADS;
	    }
	} else {
	    synchronized (this) {
		int delta = threadCount - Configuration.SCHEDULER_THREADS;
		destroyThreads(delta);
		this.threadCount = Configuration.SCHEDULER_THREADS;
		this.notifyAll();
	    }
	}
	return false;
    }

    private void destroyThreads(int delta) {
	for (int i = 0; i < delta; i++) {
	    SchedulerThreadImpl remove = threads.remove(0);
	    remove.shutdown();
	}
    }

    public List<JobController> exportJobs() {
	ArrayList<JobController> jobs = new ArrayList<JobController>();
	Collection<SimpleJob> values = this.allJobStore.values();
	for (SimpleJob job : values) {
	    jobs.add(job);
	}
	return jobs;
    }

    public List<SchedulerThread> exportThreads() {
	ArrayList<SchedulerThread> threads = new ArrayList<SchedulerThread>();
	threads.addAll(this.threads);
	return threads;
    }
}
