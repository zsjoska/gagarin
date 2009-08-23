package ro.gagarin.scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class Scheduler {

	private static final transient Logger LOG = Logger
			.getLogger(Scheduler.class);
	private ArrayList<SchedulerThread> threads = new ArrayList<SchedulerThread>();
	private final int threadCount;
	private HashMap<Long, RunableJob> pendingJobStore = new HashMap<Long, RunableJob>();
	private HashMap<Long, RunableJob> allJobStore = new HashMap<Long, RunableJob>();

	public Scheduler() {
		// TODO: make this value configurable
		this(10);
	}

	public Scheduler(int threadCount) {
		this.threadCount = threadCount;
	}

	public void start() {
		for (int i = 0; i < threadCount; i++) {
			SchedulerThread thread = new SchedulerThread(this);
			thread.start();
			threads.add(thread);
		}
	}

	public boolean shutdown() {
		return false;
	}

	private RunableJob getNextJob() {
		long minNextRun = Long.MAX_VALUE;
		RunableJob nextJob = null;
		for (RunableJob job : this.pendingJobStore.values()) {
			long nextRun = job.getNextRun();
			if (nextRun < minNextRun) {
				minNextRun = nextRun;
				nextJob = job;
			}
		}
		return nextJob;
	}

	public void releaseJob(RunableJob nextJob) {
		synchronized (this) {
			nextJob.markExecuted();
			this.pendingJobStore.put(nextJob.getId(), nextJob);
			LOG.debug("Job #" + nextJob.getId()
					+ " next execution is planned at " + nextJob.getNextRun());
			this.notify();
		}
	}

	public RunableJob waitNextJob() throws InterruptedException {
		RunableJob nextJob = null;
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
		
		if(nextJob !=null && nextJob.getNextRun()==0){
			// this was removed already from the stores
			nextJob.destroyJob();
		}
		
		if (toWait < 10) {
			if (toWait > 0) {
				Thread.sleep(toWait);
			}
			return nextJob;
		}

		return null;
	}

	public long scheduleJob(ScheduledJob job) {
		if (job.getId() == null) {
			job.setId(ScheduledJob.getNextId());
		}
		synchronized (this) {
			RunableJob runableJob = new RunableJob(job);
			this.pendingJobStore.put(job.getId(), runableJob);
			this.allJobStore.put(job.getId(), runableJob);
			this.notify();
		}
		return job.getId();
	}

	public synchronized void updateJobRate(Long id, Long rate) {
		RunableJob job = this.allJobStore.get(id);
		if (job != null) {
			job.setPeriod(rate);
			this.notify();
		} else
			LOG.error("Job was not found in job store:" + id);
	}

	public synchronized void triggerExecution(Long id) {
		RunableJob job = this.allJobStore.get(id);
		if (job != null) {
			job.markToExecuteNow();
			this.notify();
		} else
			LOG.error("Job was not found in job store:" + id);
	}
}
