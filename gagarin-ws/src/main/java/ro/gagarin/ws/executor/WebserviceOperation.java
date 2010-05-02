package ro.gagarin.ws.executor;

import org.apache.log4j.Logger;

import ro.gagarin.ApplicationInitializer;
import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;
import ro.gagarin.utils.Statistic;

public abstract class WebserviceOperation {

    protected static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();
    private static final transient Logger LOG = Logger.getLogger(ApplicationInitializer.class);

    private final String sessionString;

    private Session session = null;

    private AppLog applog;

    private final Class<?> opClass;

    private final boolean requiresLogin;

    public WebserviceOperation(String session, Class<?> opClass) {
	this(true, session, opClass);
    }

    public WebserviceOperation(boolean requiresLogin, String session, Class<?> opClass) {
	this.requiresLogin = requiresLogin;
	this.sessionString = session;
	this.opClass = opClass;
    }

    public void performOperation() throws ExceptionBase {
	prepareSession();
	if (applog != null) {
	    applog.debug(opClass.getSimpleName() + " " + this);
	} else {
	    LOG.debug(opClass.getSimpleName() + " " + this);
	}
	prepareManagers(getSession());
	prepare();
	execute();
	finish();
    }

    // TODO: make protected
    public abstract void execute() throws ExceptionBase;

    public abstract Statistic getStatistic();

    public abstract void prepareManagers(Session session) throws ExceptionBase;

    public Session getSession() {
	return session;
    }

    public AppLog getApplog() {
	return applog;
    }

    public void prepareSession() throws SessionNotFoundException, LoginRequiredException {
	if (getSessionString() == null || getSessionString().length() == 0) {
	    throw new SessionNotFoundException(getSessionString());
	}
	SessionManager sessionManager = FACTORY.getSessionManager();
	this.session = sessionManager.acquireSession(this.getSessionString());
	this.applog = FACTORY.getLogManager(session, opClass);
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
