/**
 * 
 */
package ro.gagarin.ws.authentication;

import org.apache.log4j.Logger;

import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;

/**
 * @author ZsJoska
 * 
 */
public class CreateSessionOP extends WebserviceOperation {

    private static final Statistic STAT_CREATE_SESSION = new Statistic("ws.auth.createSession");
    private static final transient Logger LOG = Logger.getLogger(CreateSessionOP.class);

    private final String language;
    private final String reason;

    private String sessionString = null;

    public CreateSessionOP(String language, String reason) {
	super(null, CreateSessionOP.class);
	this.language = language;
	this.reason = reason;
    }

    @Override
    public void execute() {
	SessionManager sessionManager = FACTORY.getSessionManager();
	String language = this.language;
	if (language == null) {
	    // TODO move this to the configuration
	    language = "en_us";
	}

	Session session = sessionManager.createSession(language, reason, FACTORY);
	this.sessionString = session.getSessionString();
	LOG.info("Session created:" + session.getId() + "; reason:" + session.getReason() + "; language:"
		+ session.getLanguage());
    }

    @Override
    public Statistic getStatistic() {
	return STAT_CREATE_SESSION;
    }

    public String getSessionString() {
	return this.sessionString;
    }

    @Override
    public void prepareSession() throws SessionNotFoundException {
	// just to override the default: do nothing
    }

    @Override
    public void releaseSession() {
	// just to override the default: do nothing
    }
}
