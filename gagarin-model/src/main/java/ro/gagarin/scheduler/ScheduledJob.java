package ro.gagarin.scheduler;

import ro.gagarin.BaseEntity;

/**
 * A publicly available job base for use through the entire application as a
 * base for creating Scheduled jobs. Provides no control over the job.
 * 
 */
public abstract class ScheduledJob extends BaseEntity {

    private final String name;
    private final long initialWait;
    private final long period;

    /**
     * The number of time to execute this job. <code>-1</code> means infinite.
     */
    private final int count;

    /**
     * Creates a ScheduledJob instance to be executed infinitely with a given
     * period and initial wait.
     * 
     * @param name
     *            the name of the job
     * @param initialWait
     *            the initial time to wait before executes for the first time
     * @param period
     *            the execution period
     */
    public ScheduledJob(String name, long initialWait, long period) {
	this.name = name;
	this.initialWait = initialWait;
	this.period = period;
	this.count = -1;
    }

    /**
     * Creates a ScheduledJob instance to be executed a given times with given
     * period. This constructor provides access to all available setup for a
     * job.
     * 
     * @param name
     *            the name of the job
     * @param initialWait
     *            the initial wait time before invoking the job for the first
     *            time
     * @param period
     *            the execution period
     * @param count
     *            the execution count
     */
    public ScheduledJob(String name, long initialWait, long period, int count) {
	this.name = name;
	this.initialWait = initialWait;
	this.period = period;
	if (this.period <= 0) {
	    count = 1;
	}
	this.count = count;
    }

    /**
     * Creates a ScheduledJob instance to be executed only once after a given
     * period of wait time.
     * 
     * @param name
     *            the name of the job
     * @param initialWait
     *            the time to wait before executing the job
     */
    public ScheduledJob(String name, long initialWait) {
	this.name = name;
	this.initialWait = initialWait;
	this.period = 0;
	this.count = 1;
    }

    /**
     * Creates a ScheduledJob instance to be executed only once without any
     * delay.
     * 
     * @param name
     *            the name of the job
     */
    public ScheduledJob(String name) {
	this.name = name;
	this.initialWait = 0;
	this.period = 0;
	this.count = 1;
    }

    /**
     * Abstract method to implement the functionality of the job. This method is
     * invoked when the scheduler schedules the job.
     * 
     * @param jobController
     *            a {@link JobController} instance provided for the execution.
     *            The job could get session and logger instance through it if
     *            requested during the creation.<br>
     *            Additionally, provides the basic control operations over the
     *            current job.
     * @throws Exception
     */
    abstract public void execute(JobController jobController) throws Exception;

    /**
     * Cleanup method for the job. This method is before the job will be
     * destroyed.
     */
    public void cleanup() {

    }

    /**
     * Returns the name of the job <br>
     * <b>Note: read only, reflects the construction value</b>
     * 
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     * Returns the inital wait time of the job<br>
     * <b>Note: read only, reflects the construction value</b>
     * 
     * @return
     */
    public long getInitialWait() {
	return initialWait;
    }

    /**
     * Returns the period of the job <br>
     * <b>Note: read only, reflects the construction value</b>
     * 
     * @return
     */
    public long getPeriod() {
	return period;
    }

    @Override
    public String toString() {
	return "ScheduledJob [count=" + count + ", initialWait=" + initialWait + ", name=" + name + ", period="
		+ period + ", getId()=" + getId() + "]";
    }

    /**
     * Returns the execution count of the job. <br>
     * <b>Note: read only, reflects the construction value</b>
     * 
     * @return
     */
    public int getCount() {
	return count;
    }
}
