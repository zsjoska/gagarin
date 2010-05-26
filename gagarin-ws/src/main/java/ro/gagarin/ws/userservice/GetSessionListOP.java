package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.SessionManager;
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
    protected void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	sessionManager = FACTORY.getSessionManager();
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	authManager.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.AUDIT);
	List<Session> sessions = sessionManager.getSessionList();

	this.sessionList = WSConversionUtils.convertToSessionList(sessions);
    }

    public List<WSExportedSession> getSessionList() {
	return this.sessionList;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input
    }

}
