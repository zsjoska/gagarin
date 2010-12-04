package ro.gagarin.scheduler;

/**
 * A job with basic controls given to the executing code.<br>
 * A job this way has a limited control over itself.
 */
public interface JobController extends GenericJob {

    void markToExecuteNow();

    void markDone();

}