/**
 * 
 */
package ro.gagarin.ws.authentication;

import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;

/**
 * @author ZsJoska
 * 
 */
public class CreateSession extends WebserviceOperation {

    private static final Statistic STAT_CREATE_SESSION = new Statistic("ws.auth.createSession");

    private final String language;
    private final String reason;

    public CreateSession(String language, String reason) {
	this.language = language;
	this.reason = reason;
    }

    @Override
    public void execute() {
	// TODO Auto-generated method stub

    }

    @Override
    public Statistic getStatistic() {
	return STAT_CREATE_SESSION;
    }

    public String getSessionString() {
	return null;
    }

}
