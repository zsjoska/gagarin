package ro.gagarin.scheduler;

import ro.gagarin.BaseEntity;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;

/**
 * A publicly available job base for use through the entire application as a
 * base for creating Scheduled jobs. Provides no control over the job.
 * 
 * @author ZsJoska
 * 
 */
public abstract class ScheduledJob extends BaseEntity {

    private final String name;
    private final long initialWait;
    private final long period;

    /**
     * The number of time to execute this job. <code>-1</code> means infinite.
     */
    private int count;

    public ScheduledJob(String name, long initialWait, long period) {
	this.name = name;
	this.initialWait = initialWait;
	this.period = period;
	this.count = -1;
    }

    public ScheduledJob(String name, long initialWait, long period, int count) {
	this.name = name;
	this.initialWait = initialWait;
	this.period = period;
	this.count = count;
    }

    public ScheduledJob(String name, long initialWait) {
	this.name = name;
	this.initialWait = initialWait;
	this.period = 0;
	this.count = 1;
    }

    abstract public void execute(Session session, AppLog log, JobController jobController) throws Exception;

    public String getName() {
	return name;
    }

    public long getInitialWait() {
	return initialWait;
    }

    public long getPeriod() {
	return period;
    }

    @Override
    public String toString() {
	return "ScheduledJob [count=" + count + ", initialWait=" + initialWait + ", name=" + name + ", period="
		+ period + ", getId()=" + getId() + "]";
    }

    public void setCount(int count) {
	this.count = count;
    }

    public int getCount() {
	return count;
    }
}
