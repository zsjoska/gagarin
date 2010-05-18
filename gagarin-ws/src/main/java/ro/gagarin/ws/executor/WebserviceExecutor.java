/**
 * 
 */
package ro.gagarin.ws.executor;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.utils.Statistic;

/**
 * @author ZsJoska
 * 
 */
public class WebserviceExecutor {
    private static final transient Logger LOG = Logger.getLogger(WebserviceExecutor.class);

    public static void execute(WebserviceOperation op) throws WSException {
	long start = System.currentTimeMillis();
	LOG.debug("Executing WSOperation " + op.getClass().getSimpleName());
	try {
	    op.performOperation();
	} catch (ExceptionBase e) {
	    LOG.error("Exception executing the operation: " + op.getClass().getName(), e);
	    throw new WSException(e);
	} catch (Exception e) {
	    LOG.error("Exception executing the operation: " + op.getClass().getName(), e);
	    throw new WSException(ErrorCodes.INTERNAL_ERROR, e.getMessage());
	} finally {
	    try {
		if (op.getSession() != null) {
		    op.releaseSession();
		}
	    } catch (Exception e) {
		LOG.error("Exception releasing the session after the operation: " + op.getClass().getName(), e);
	    }
	    long duration = System.currentTimeMillis() - start;
	    Statistic.getByName(op.getClass().getName()).addDuration(duration);
	    LOG.info(op.getClass().getName() + " took " + duration + "ms");
	}
    }
}
