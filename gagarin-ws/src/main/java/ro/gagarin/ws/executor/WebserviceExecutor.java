/**
 * 
 */
package ro.gagarin.ws.executor;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.ws.WSException;

/**
 * @author ZsJoska
 * 
 */
public class WebserviceExecutor {
    private static final transient Logger LOG = Logger.getLogger(WebserviceExecutor.class);

    public static void execute(WebserviceOperation op) throws WSException {
	long start = System.currentTimeMillis();
	LOG.debug("Executing WSOperation " + op.getClass().getName());
	try {
	    op.prepareSession();
	    op.prepareManagers(op.getSession());
	    op.prepare();
	    op.execute();
	    op.finish();
	} catch (ExceptionBase e) {
	    LOG.error("Exception executing the operation: " + op.getClass().getName(), e);
	    throw new WSException(e);
	} finally {
	    try {
		op.releaseSession();
	    } catch (Exception e) {
		LOG.error("Exception releasing the session after the operation: " + op.getClass().getName(), e);
	    }
	    long duration = System.currentTimeMillis() - start;
	    op.getStatistic().addDuration(duration);
	    LOG.info(op.getClass().getName() + " took " + duration + "ms");
	}
    }
}
