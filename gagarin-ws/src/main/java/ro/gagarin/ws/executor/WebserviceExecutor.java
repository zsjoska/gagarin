/**
 * 
 */
package ro.gagarin.ws.executor;

/**
 * @author ZsJoska
 * 
 */
public class WebserviceExecutor {

    public static void execute(WebserviceOperation op) {
	op.execute();
    }

}
