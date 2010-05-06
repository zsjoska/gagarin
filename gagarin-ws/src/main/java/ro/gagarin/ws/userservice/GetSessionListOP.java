package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetSessionListOP extends WebserviceOperation {

    private List<WSExportedSession> sessionList;

    private AuthorizationManager authManager;

    private SessionManager sessionManager;

    public GetSessionListOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
	sessionManager = FACTORY.getSessionManager();
    }

    @Override
    public void execute() throws ExceptionBase {

	authManager.requiresPermission(getSession(), PermissionEnum.ADMIN_OPERATION);
	List<Session> sessions = sessionManager.getSessionList();

	this.sessionList = WSConversionUtils.convertToSessionList(sessions);
    }

    public List<WSExportedSession> getSessionList() {
	return this.sessionList;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }

}
