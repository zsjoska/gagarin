package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.CommonControlEntities;
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

    private SessionManager sessionManager;

    public GetSessionListOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, CommonControlEntities.ADMIN_CE, PermissionEnum.AUDIT);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	sessionManager = FACTORY.getSessionManager();
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	List<Session> sessions = sessionManager.getSessionList();
	this.sessionList = WSConversionUtils.convertToSessionList(sessions);
	getApplog().debug("Returning " + sessionList.size() + " sessions");
    }

    public List<WSExportedSession> getSessionList() {
	return this.sessionList;
    }
}
