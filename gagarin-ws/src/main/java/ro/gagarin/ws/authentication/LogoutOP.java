/**
 * 
 */
package ro.gagarin.ws.authentication;

import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.ws.executor.WebserviceOperation;

public class LogoutOP extends WebserviceOperation {

    private SessionManager sessionManager;

    public LogoutOP(String sessionId) {
	super(false, sessionId);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	sessionManager = FACTORY.getSessionManager();
    }

    @Override
    public void execute() throws ExceptionBase {
	sessionManager.logout(getSessionString());
	getApplog().info("Logout completed");
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }

}
