package ro.gagarin.scheduler;

public class SchedulerThread extends Thread {

	private final Scheduler parent;

	public SchedulerThread(Scheduler parent) {
		this.parent = parent;
	}

	@Override
	public void run() {
		try {
			while (!parent.shutdown()) {
				RunableJob nextJob = parent.waitNextJob();
				if (nextJob == null) {
					continue;
				}
				nextJob.run();
				parent.releaseJob(nextJob);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
		}

	}
}