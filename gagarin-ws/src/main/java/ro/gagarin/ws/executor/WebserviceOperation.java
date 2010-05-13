package ro.gagarin.ws.executor;

import org.apache.log4j.Logger;

import ro.gagarin.ApplicationInitializer;
import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;
import ro.gagarin.utils.FieldValidator;

public abstract class WebserviceOperation {

    protected static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();
    private static final transient Logger LOG = Logger.getLogger(ApplicationInitializer.class);

    private String sessionString;

    private Session session = null;

    private AppLog applog;

    private final boolean requiresLogin;

    public WebserviceOperation(String sessionId) {
	this(true, sessionId);
    }

    public WebserviceOperation(boolean requiresLogin, String sessionId) {
	this.requiresLogin = requiresLogin;
	this.sessionString = sessionId;
    }

    public void performOperation() throws ExceptionBase {
	prepareSession();
	checkInput(getSession());
	if (applog != null) {
	    applog.debug(this.getClass().getSimpleName() + " " + this);
	} else {
	    LOG.debug(this.getClass().getSimpleName() + " " + this);
	}
	prepareManagers(getSession());
	prepare();
	execute();
	finish();
    }

    // TODO: make protected
    public abstract void execute() throws ExceptionBase;

    public abstract void prepareManagers(Session session) throws ExceptionBase;

    public abstract void checkInput(Session session) throws ExceptionBase;

    public Session getSession() {
	return session;
    }

    public AppLog getApplog() {
	return applog;
    }

    public void prepareSession() throws SessionNotFoundException, LoginRequiredException {
	try {
	    this.sessionString = FieldValidator.checkStringValue(sessionString, "sessionId", 100);
	} catch (FieldRequiredException e) {
	    throw new SessionNotFoundException(getSessionString());
	}
	SessionManager sessionManager = FACTORY.getSessionManager();
	this.session = sessionManager.acquireSession(this.getSessionString());
	this.applog = FACTORY.getLogManager().getLoggingSession(session, this.getClass());
	if (requiresLogin) {
	    FACTORY.getAuthorizationManager(session).requireLogin(session);
	}
    }

    public void prepare() {
    }

    public void finish() {
    }

    public void releaseSession() {
	FACTORY.releaseSession(this.session);
    }

    public String getSessionString() {
	return sessionString;
    }

}
