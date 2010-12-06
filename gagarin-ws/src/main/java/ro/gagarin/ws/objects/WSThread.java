package ro.gagarin.ws.objects;

import ro.gagarin.scheduler.SchedulerThread;

public class WSThread {

    private String name;
    private WSJob activeJob = null;

    public WSThread(SchedulerThread thread) {
	this.name = thread.getName();
	if (thread.getActiveJob() != null) {
	    this.activeJob = new WSJob(thread.getActiveJob());
	}
    }

    public WSThread() {
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public WSJob getActiveJob() {
	return activeJob;
    }

    public void setActiveJob(WSJob activeJob) {
	this.activeJob = activeJob;
    }

    @Override
    public String toString() {
	return "WSThread [activeJob=" + activeJob + ", name=" + name + "]";
    }

}
