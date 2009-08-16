package ro.gagarin.scheduler;

import java.util.ArrayList;
import java.util.LinkedList;

public class Scheduler {

	private ArrayList<SchedulerThread> threads;
	private final int threadCount;
	private LinkedList<RunableJob> x;

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
		for (RunableJob job : this.x) {
			long nextRun = job.getNextRun();
			if(nextRun < minNextRun){
				minNextRun = nextRun;
				nextJob = job;
			}
		}
		return nextJob;
	}

	public void releaseJob(RunableJob nextJob) {
		
		
	}

	public synchronized RunableJob waitNextJob() throws InterruptedException {
		RunableJob nextJob;
		long toWait;
		synchronized (this) {
			nextJob = this.getNextJob();
			toWait = nextJob.getNextRun() - System.currentTimeMillis();
			if(toWait < 10){
				this.x.remove(nextJob);
			} else {
				this.wait(toWait);
			}
		}
		if(toWait <10){
			if(toWait > 0){
				Thread.sleep(toWait);
			}
			return nextJob;
		}
		
		return null;
	}

}
