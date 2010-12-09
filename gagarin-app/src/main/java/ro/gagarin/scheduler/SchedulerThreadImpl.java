package ro.gagarin.scheduler;

public class SchedulerThreadImpl extends Thread implements SchedulerThread {

    private final Scheduler parent;
    private boolean shutDown = false;
    private SimpleJob activeJob = null;

    public SchedulerThreadImpl(Scheduler parent, int index) {
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
		this.activeJob = nextJob;
		nextJob.run();
		parent.releaseJob(nextJob);
		this.activeJob = null;
	    }
	} catch (InterruptedException e) {
	    // TODO:(3) handle exception
	}

    }

    public GenericJob getActiveJob() {
	return this.activeJob;
    }

    public void shutdown() {
	this.shutDown = true;

    }
}
