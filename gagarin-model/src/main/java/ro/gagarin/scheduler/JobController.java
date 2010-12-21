package ro.gagarin.scheduler;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;

/**
 * A job with basic controls given to the executing code.<br>
 * A job this way has a limited control over itself.
 */
public interface JobController extends GenericJob {

    void markToExecuteNow();

    void markDone();

    Session getSession() throws OperationException;

    AppLog getLogger() throws OperationException;

}