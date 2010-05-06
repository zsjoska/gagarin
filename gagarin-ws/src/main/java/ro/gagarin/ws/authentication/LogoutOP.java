/**
 * 
 */
package ro.gagarin.ws.authentication;

import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;

public class LogoutOP extends WebserviceOperation {

    private static final Statistic STAT_LOGOUT = Statistic.getByName("ws.auth.logout");
    private SessionManager sessionManager;

    public LogoutOP(String sessionId) {
	super(false, sessionId, LogoutOP.class);
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
    public Statistic getStatistic() {
	return STAT_LOGOUT;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }

}