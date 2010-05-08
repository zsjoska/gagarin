package ro.gagarin.scheduler;

import org.apache.log4j.Logger;

import ro.gagarin.utils.Statistic;

class SimpleJob implements JobController {

    private static final transient Logger LOG = Logger.getLogger(SimpleJob.class);

    private final ScheduledJob job;

    private int toExecute;

    private long lastRun;

    private long period;

    public SimpleJob(ScheduledJob job) {
	this.job = job;
	this.period = job.getPeriod();
	this.lastRun = (System.currentTimeMillis() + job.getInitialWait()) - job.getPeriod();
	if (job.getPeriod() == 0) {
	    // once to be executed
	    this.toExecute = 1;
	} else {
	    this.toExecute = job.getCount();
	}
    }

    public void run() {

	long start = System.currentTimeMillis();
	long delay = start - this.getNextRun();

	// TODO: move to a base method

	if (delay > 10) {
	    String msg = "Job #" + this.getId() + " execution delay:" + delay;
	    if (delay < 20) {
		LOG.debug(msg);
	    } else if (delay < 50) {
		LOG.warn(msg);
	    } else if (delay < 100) {
		LOG.info(msg);
	    } else if (delay >= 100) {
		LOG.error(msg);
	    }
	}

	try {

	    LOG.debug("Executing job " + getJob().getName() + "#" + getJob().getId());

	    long jobStart = System.currentTimeMillis();

	    getJob().execute(null, null, this);

	    Statistic.getByName("job.simple.effective." + getJob().getName()).add(jobStart);

	    LOG.debug("Finished job " + getJob().getName() + "#" + getJob().getId());

	} catch (Exception e) {
	    LOG.error("Exception executing job " + getJob().getName() + "#" + getJob().getId(), e);
	}

	long end = System.currentTimeMillis();
	if (end - start > this.getPeriod()) {
	    LOG.error("Job #" + this.getId() + " execution takes longer than the period:" + this.getPeriod()
		    + "; duration:" + (end - start));
	}
	Statistic.getByName("job.simple.overall." + getJob().getName()).add(start);
    }

    public void destroyJob() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ro.gagarin.scheduler.JobController#getNextRun()
     */
    public long getNextRun() {
	// negative is infinite, positive is to be executed
	if (toExecute < 0 || toExecute > 0) {
	    return this.lastRun + this.period;
	}
	return 0;
    }

    public Long getId() {
	return this.getJob().getId();
    }

    public void markExecuted() {
	this.lastRun += this.period;
	if (this.toExecute > 0)
	    this.toExecute--;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ro.gagarin.scheduler.JobController#setPeriod(long)
     */
    public void setPeriod(long period) {
	this.period = period;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ro.gagarin.scheduler.JobController#getPeriod()
     */
    public long getPeriod() {
	return period;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ro.gagarin.scheduler.JobController#markToExecuteNow()
     */
    public void markToExecuteNow() {
	this.lastRun = System.currentTimeMillis() - period;
    }

    public ScheduledJob getJob() {
	return job;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ro.gagarin.scheduler.JobController#markDone()
     */
    public void markDone() {
	this.toExecute = 0;
    }
}
