package ro.gagarin.scheduler;

/**
 * Generic description of a job, safe to give to any calling code
 */
public interface GenericJob {

    String getName();

    long getPeriod();

    Long getLastExecution();

    Long getNextExecution();

    Double getPercentComplete();

}
