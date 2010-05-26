/**
 * 
 */
package ro.gagarin.ws.authentication;

import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.SessionManager;
import ro.gagarin.session.Session;
import ro.gagarin.ws.executor.WebserviceOperation;

public class LogoutOP extends WebserviceOperation {

    private SessionManager sessionManager;

    public LogoutOP(String sessionId) {
	super(false, sessionId);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	sessionManager = FACTORY.getSessionManager();
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	sessionManager.logout(getSessionString());
	getApplog().info("Logout completed");
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input
    }

}
