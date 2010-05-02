package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetSessionListOP extends WebserviceOperation {

    private static final Statistic STAT_GET_SESSION_LIST = new Statistic("ws.userserservice.getSessionList");

    private List<WSExportedSession> sessionList;

    private AuthorizationManager authManager;

    private SessionManager sessionManager;

    public GetSessionListOP(String sessionId) {
	super(sessionId, GetSessionListOP.class);
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

    @Override
    public Statistic getStatistic() {
	return STAT_GET_SESSION_LIST;
    }

    public List<WSExportedSession> getSessionList() {
	return this.sessionList;
    }

}
