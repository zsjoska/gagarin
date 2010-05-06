/**
 * 
 */
package ro.gagarin.ws.authentication;

import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;

public class CreateSessionOP extends WebserviceOperation {

    private String language;
    private String reason;

    private String sessionString = null;
    private SessionManager sessionManager;

    public CreateSessionOP(String language, String reason) {
	super(false, null);
	this.language = language;
	this.reason = reason;
    }

    @Override
    public void prepareManagers(Session session) {
	sessionManager = FACTORY.getSessionManager();
    }

    @Override
    public void execute() {
	String language = this.language;
	if (language == null) {
	    // TODO move this to the configuration
	    language = "en_us";
	}

	Session session = sessionManager.createSession(language, reason, FACTORY);
	this.sessionString = session.getSessionString();
	AppLog log = FACTORY.getLogManager(session, CreateSessionOP.class);
	log.info("Session created:" + session.getSessionString() + "; reason:" + session.getReason() + "; language:"
		+ session.getLanguage());
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

    @Override
    public String toString() {
	return "CreateSessionOP [language=" + language + ", reason=" + reason + "]";
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// TODO: custom check for language
	this.reason = FieldValidator.checkStringValue(reason, "reason", 20);
    }
}
