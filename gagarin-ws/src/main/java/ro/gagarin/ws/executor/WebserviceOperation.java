package ro.gagarin.ws.executor;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.manager.SessionManager;
import ro.gagarin.session.Session;
import ro.gagarin.utils.FieldValidator;

/**
 * Base Class for a web service operation.<br>
 * This class encapsulates all operations performed through Web Services.
 * Provides implementation for the common tasks and abstract methods for
 * performing the operation specific tasks.
 * 
 * 
 * @author zsido
 * 
 */
public abstract class WebserviceOperation {

    protected static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private static final transient Logger LOG = Logger.getLogger(WebserviceOperation.class);

    private String sessionString;

    private Session session = null;

    private AppLog applog;

    private final boolean requiresLogin;

    private AuthorizationManager authorizationManager;

    /**
     * Constructs a base Web Service operation on a given session.
     * 
     * @param sessionId
     */
    public WebserviceOperation(String sessionId) {
	this(true, sessionId);
    }

    public WebserviceOperation(boolean requiresLogin, String sessionId) {
	this.requiresLogin = requiresLogin;
	this.sessionString = sessionId;
    }

    /**
     * Executes the operation.<br>
     * This method calls the following methods in the specified order:<br>
     * <li>
     * {@link #prepareSession()}</li><br>
     * <li> {@link #checkInput(Session)}</li><br>
     * <li>
     * {@link #checkPermissions(Session, AuthorizationManager)}</li><br>
     * <li>
     * {@link #prepareManagers(Session)}</li><br>
     * <li> {@link #execute(Session)}</li><br>
     * <li> {@link #finish()}</li><br>
     * <br>
     * 
     * @throws ExceptionBase
     */
    public void performOperation() throws ExceptionBase {

	prepareSession();

	checkInput(getSession());

	checkPermissions(getSession(), getAuthorizationManager());

	if (applog != null) {
	    applog.debug(this.getClass().getSimpleName() + " " + this);
	} else {
	    LOG.debug(this.getClass().getSimpleName() + " " + this);
	}
	prepareManagers(getSession());
	execute(getSession());
	finish();
    }

    /**
     * The implementation should verify the user input in this method.
     * 
     * @param session
     * @throws ExceptionBase
     */
    protected abstract void checkInput(Session session) throws ExceptionBase;

    /**
     * The implementation should verify the application level permissions for
     * the current operation.
     * 
     * @param session
     * @param authMgr
     * @throws ExceptionBase
     */
    protected abstract void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase;

    /**
     * Convenient method allowing the application to create the additionally
     * required managers
     * 
     * @param session
     * @throws ExceptionBase
     */
    protected abstract void prepareManagers(Session session) throws ExceptionBase;

    /**
     * This method is the right location for implementing the actual tasks of
     * the operation.
     * 
     * @param session
     * @throws ExceptionBase
     */
    protected abstract void execute(Session session) throws ExceptionBase;

    /**
     * Returns the current session
     * 
     * @return
     */
    public Session getSession() {
	return session;
    }

    /**
     * Returns the application logger associated with the current session
     * 
     * @return
     */
    protected AppLog getApplog() {
	return applog;
    }

    /**
     * Performs the initialization of the session. This implementation werifies
     * if we have received a correct sessionId, finds the session in our cache
     * and acquires it for exclusive use.
     * 
     * @throws SessionNotFoundException
     * @throws LoginRequiredException
     */
    protected void prepareSession() throws SessionNotFoundException, LoginRequiredException {

	try {
	    this.sessionString = FieldValidator.requireStringValue(sessionString, "sessionId", 100);
	} catch (FieldRequiredException e) {
	    LOG.error("Missing required session string", e);
	    throw new SessionNotFoundException(getSessionString());
	}
	SessionManager sessionManager = FACTORY.getSessionManager();
	this.session = sessionManager.acquireSession(this.getSessionString());
	this.applog = FACTORY.getLogManager().getLoggingSession(session, this.getClass());
	authorizationManager = FACTORY.getAuthorizationManager();
	if (requiresLogin) {
	    authorizationManager.requireLogin(session);
	}

    }

    /**
     * Placeholder method for performing additional tasks.
     */
    protected void finish() {
    }

    /**
     * Releases the session
     */
    public void releaseSession() {
	FACTORY.releaseSession(this.session);
    }

    /**
     * Returns the session string for this session
     * 
     * @return
     */
    protected String getSessionString() {
	return sessionString;
    }

    /**
     * Returns an authorization manager assigned with the current session
     * 
     * @return
     */
    protected AuthorizationManager getAuthorizationManager() {
	return this.authorizationManager;
    }

}
