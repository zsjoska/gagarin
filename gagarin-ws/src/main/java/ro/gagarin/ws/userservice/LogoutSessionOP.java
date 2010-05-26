package ro.gagarin.ws.userservice;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.SessionManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;

public class LogoutSessionOP extends WebserviceOperation {
    private final String otherSessionId;

    private SessionManager sessionManager;

    public LogoutSessionOP(String sessionId, String otherSessionId) {
	super(sessionId);
	this.otherSessionId = otherSessionId;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringValue(otherSessionId, "otherSessionId", 50);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.ADMIN);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	sessionManager = FACTORY.getSessionManager();
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	sessionManager.logout(otherSessionId);
	getApplog().info("LogoutSession " + otherSessionId);

    }

    @Override
    public String toString() {
	return "LogoutSessionOP [otherSessionId=" + otherSessionId + "]";
    }
}
