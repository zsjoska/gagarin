package ro.gagarin.scheduler;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;
import ro.gagarin.utils.Statistic;

class SimpleJob implements JobController {

    private static final transient Logger LOG = Logger.getLogger(SimpleJob.class);

    private final ScheduledJob job;

    private int toExecute;

    private long lastRun;

    private long period;

    private Double percentComplete = -1.0;

    private final OperationException creationStack;

    public SimpleJob(ScheduledJob job) {
	this.creationStack = new OperationException(ErrorCodes.INTERNAL_ERROR, "Simple job creation");
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

	// TODO:(2) move to a base method

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

	    ScheduledJob theJob = getJob();
	    percentComplete = -1.0;
	    theJob.execute(this);
	    percentComplete = 100.0;

	    Statistic.getByName("job.simple.effective." + getJob().getName()).add(jobStart);

	    LOG.debug("Finished job " + getJob().getName() + "#" + getJob().getId());

	} catch (Exception e) {
	    LOG.error("Exception executing job " + getJob().getName() + "#" + getJob().getId(), e);
	}

	reportPeriodOverrun(start);

	Statistic.getByName("job.simple.overall." + getJob().getName()).add(start);
    }

    protected void reportPeriodOverrun(long start) {
	long end = System.currentTimeMillis();
	if (this.getPeriod() > 0 && end - start > this.getPeriod()) {
	    // for jobs that are executed once this is always true: period = 0
	    // so we have to exclude it to not log the false error
	    LOG.error("Job " + this.getName() + "#" + this.getId() + " execution takes longer than the period:"
		    + this.getPeriod() + "; duration:" + (end - start));
	}
    }

    public void destroyJob() {
	getJob().cleanup();
    }

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

    public void setPeriod(long period) {
	this.period = period;
    }

    public long getPeriod() {
	return period;
    }

    public void markToExecuteNow() {
	this.lastRun = System.currentTimeMillis() - period;
    }

    public ScheduledJob getJob() {
	return job;
    }

    public void markDone() {
	this.toExecute = 0;
    }

    @Override
    public Long getLastExecution() {
	return lastRun;
    }

    @Override
    public String getName() {
	return getJob().getName();
    }

    @Override
    public Long getNextExecution() {
	if (getNextRun() != 0) {
	    return getNextRun();
	}
	return null;
    }

    @Override
    public Double getPercentComplete() {
	return percentComplete;
    }

    public void setPercentComplete(Double percentComplete) {
	this.percentComplete = percentComplete;
    }

    @Override
    public Session getSession() throws OperationException {
	throw new OperationException(ErrorCodes.SESSION_NOT_FOUND, this.creationStack);
    }

    @Override
    public AppLog getLogger() throws OperationException {
	throw new OperationException(ErrorCodes.SESSION_NOT_FOUND, this.creationStack);
    }
}
