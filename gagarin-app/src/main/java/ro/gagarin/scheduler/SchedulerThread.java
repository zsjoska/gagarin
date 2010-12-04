package ro.gagarin.scheduler;

public class SchedulerThread extends Thread {

    private final Scheduler parent;
    private boolean shutDown = false;

    public SchedulerThread(Scheduler parent, int index) {
	super("SCHEDULER" + index);
	this.parent = parent;
	setDaemon(false);
    }

    @Override
    public void run() {
	try {
	    while (!parent.shutdown() && !this.shutDown) {
		SimpleJob nextJob = parent.waitNextJob();
		if (nextJob == null) {
		    continue;
		}
		nextJob.run();
		parent.releaseJob(nextJob);
	    }
	} catch (InterruptedException e) {
	    // TODO:(3) handle exception
	}

    }

    public void shutdown() {
	this.shutDown = true;

    }
}
