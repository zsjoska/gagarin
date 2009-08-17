package ro.gagarin.scheduler;

import java.util.ArrayList;
import java.util.HashMap;

public class Scheduler {

	private ArrayList<SchedulerThread> threads = new ArrayList<SchedulerThread>();
	private final int threadCount;
	private HashMap<Long, RunableJob> jobStore = new HashMap<Long, RunableJob>();

	public Scheduler() {
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

	public RunableJob getNextJob() {
		long minNextRun = Long.MAX_VALUE;
		RunableJob nextJob = null;
		for (RunableJob job : this.jobStore.values()) {
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
			this.jobStore.put(nextJob.getId(), nextJob);
			this.notify();
		}
	}

	public synchronized RunableJob waitNextJob() throws InterruptedException {
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
				this.jobStore.remove(nextJob.getId());
				return null;
			}

			toWait = nextRun - System.currentTimeMillis();
			if (toWait < 10) {
				this.jobStore.remove(nextJob.getId());
			} else {
				this.wait(toWait);
				return null;
			}
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
		job.setId(ScheduledJob.getNextId());
		synchronized (this) {
			this.jobStore.put(job.getId(), new RunableJob(job));
			this.notify();
		}
		return job.getId();
	}

	public synchronized void updateJobRate(Long id, Long rate) {
		RunableJob job = this.jobStore.get(id);
		if(job != null){
			job.setPeriod(rate);
			this.notify();
		}
	}
}
