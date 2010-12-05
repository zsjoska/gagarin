package ro.gagarin.ws.objects;

import ro.gagarin.scheduler.GenericJob;

public class WSJob implements GenericJob {

    private Long lastExecution;
    private String name;
    private Long nextExecution;
    private Double percentComplete;
    private long period;

    public WSJob(GenericJob job) {
	this.lastExecution = job.getLastExecution();
	this.name = job.getName();
	this.nextExecution = job.getNextExecution();
	this.percentComplete = job.getPercentComplete();
	this.period = job.getPeriod();
    }

    public WSJob() {
    }

    @Override
    public Long getLastExecution() {
	return this.lastExecution;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public Long getNextExecution() {
	return nextExecution;
    }

    @Override
    public Double getPercentComplete() {
	return percentComplete;
    }

    @Override
    public long getPeriod() {
	return period;
    }

    public void setLastExecution(Long lastExecution) {
	this.lastExecution = lastExecution;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setNextExecution(Long nextExecution) {
	this.nextExecution = nextExecution;
    }

    public void setPercentComplete(Double percentComplete) {
	this.percentComplete = percentComplete;
    }

    public void setPeriod(long period) {
	this.period = period;
    }

    @Override
    public String toString() {
	return "WSJob [lastExecution=" + lastExecution + ", name=" + name + ", nextExecution=" + nextExecution
		+ ", percentComplete=" + percentComplete + ", period=" + period + "]";
    }

}
