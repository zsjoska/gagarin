package ro.gagarin.ws.userservice;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;

public class LogoutSessionOP extends WebserviceOperation {
    private static final Statistic STAT_LOGOUT_SESSION = Statistic.getByName("ws.userserservice.logoutSession");

    private final String otherSessionId;

    private AuthorizationManager authManager;

    private SessionManager sessionManager;

    public LogoutSessionOP(String sessionId, String otherSessionId) {
	super(sessionId, LogoutSessionOP.class);
	this.otherSessionId = otherSessionId;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
	sessionManager = FACTORY.getSessionManager();
    }

    @Override
    public void execute() throws ExceptionBase {

	authManager.requiresPermission(getSession(), PermissionEnum.ADMIN_OPERATION);

	sessionManager.logout(otherSessionId);
	getApplog().info("LogoutSession " + otherSessionId);

    }

    @Override
    public Statistic getStatistic() {
	return STAT_LOGOUT_SESSION;
    }

    @Override
    public String toString() {
	return "LogoutSessionOP [otherSessionId=" + otherSessionId + "]";
    }

}
