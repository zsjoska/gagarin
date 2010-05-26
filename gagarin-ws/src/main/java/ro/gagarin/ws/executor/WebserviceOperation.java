package ro.gagarin.ws.executor;

import org.apache.log4j.Logger;

import ro.gagarin.ApplicationInitializer;
import ro.gagarin.BasicManagerFactory;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.manager.SessionManager;
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
	execute(getSession());
	finish();
    }

    // TODO:(3) make protected
    // TODO:(2) Give the session as parameter
    protected abstract void execute(Session session2) throws ExceptionBase;

    protected abstract void prepareManagers(Session session) throws ExceptionBase;

    protected abstract void checkInput(Session session) throws ExceptionBase;

    public Session getSession() {
	return session;
    }

    protected AppLog getApplog() {
	return applog;
    }

    protected void prepareSession() throws SessionNotFoundException, LoginRequiredException {
	try {
	    this.sessionString = FieldValidator.requireStringValue(sessionString, "sessionId", 100);
	} catch (FieldRequiredException e) {
	    throw new SessionNotFoundException(getSessionString());
	}
	SessionManager sessionManager = FACTORY.getSessionManager();
	this.session = sessionManager.acquireSession(this.getSessionString());
	this.applog = FACTORY.getLogManager().getLoggingSession(session, this.getClass());
	if (requiresLogin) {
	    FACTORY.getAuthorizationManager().requireLogin(session);
	}
    }

    protected void prepare() {
    }

    protected void finish() {
    }

    protected void releaseSession() {
	FACTORY.releaseSession(this.session);
    }

    protected String getSessionString() {
	return sessionString;
    }

}
