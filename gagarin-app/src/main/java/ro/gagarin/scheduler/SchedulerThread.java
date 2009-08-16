package ro.gagarin.scheduler;

public class SchedulerThread extends Thread {

	private final Scheduler parent;

	public SchedulerThread(Scheduler parent) {
		this.parent = parent;
	}
	
	@Override
	public void run() {
		
		while(!parent.shutdown()){
			RunableJob nextJob = parent.waitNextJob();
			nextJob.run();
			parent.releaseJob(nextJob);
		}
		
	}
}
