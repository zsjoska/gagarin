package ro.gagarin.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.log.LogEntry;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.Statistic;
import ro.gagarin.utils.StatisticsContainer;
import ro.gagarin.ws.executor.WebserviceExecutor;
import ro.gagarin.ws.objects.WSConfig;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.objects.WSStatistic;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;
import ro.gagarin.ws.userservice.CreateRoleWithPermissionsOP;
import ro.gagarin.ws.userservice.CreateUserOP;
import ro.gagarin.ws.userservice.DeleteRoleOP;
import ro.gagarin.ws.userservice.GetAllPermissionListOP;
import ro.gagarin.ws.userservice.GetConfigEntriesOP;
import ro.gagarin.ws.userservice.GetRoleListOP;
import ro.gagarin.ws.userservice.GetRolePermissionstOP;
import ro.gagarin.ws.userservice.GetUsersOP;
import ro.gagarin.ws.userservice.SetConfigEntryOP;
import ro.gagarin.ws.util.WSConversionUtils;

@WebService
public class UserService {

    private static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    @WebMethod
    public Long createUser(String sessionId, WSUser user) throws WSException {

	CreateUserOP createUser = new CreateUserOP(sessionId, user);
	WebserviceExecutor.execute(createUser);
	return createUser.getUserId();

    }

    @WebMethod
    public List<WSUserRole> getRoleList(String sessionId) throws WSException {

	GetRoleListOP getRoleList = new GetRoleListOP(sessionId);
	WebserviceExecutor.execute(getRoleList);
	return getRoleList.getRoleList();

    }

    @WebMethod
    public WSUserRole createRoleWithPermissions(String sessionId, String roleName, WSUserPermission[] permissions)
	    throws WSException {

	CreateRoleWithPermissionsOP createRoleWithPermissions = new CreateRoleWithPermissionsOP(sessionId, roleName,
		permissions);
	WebserviceExecutor.execute(createRoleWithPermissions);
	return createRoleWithPermissions.getRole();

    }

    @WebMethod
    public List<WSUserPermission> getAllPermissionList(String sessionId) throws WSException {

	GetAllPermissionListOP getAllPermissionList = new GetAllPermissionListOP(sessionId);
	WebserviceExecutor.execute(getAllPermissionList);
	return getAllPermissionList.getPermissionList();
    }

    @WebMethod
    public List<WSUserPermission> getRolePermissions(String sessionId, WSUserRole wsUserRole) throws WSException {

	GetRolePermissionstOP getRolePermissions = new GetRolePermissionstOP(sessionId, wsUserRole);
	WebserviceExecutor.execute(getRolePermissions);
	return getRolePermissions.getRolePermissions();

    }

    @WebMethod
    public void deleteRole(String sessionId, WSUserRole wsUserRole) throws WSException {

	DeleteRoleOP deleteRole = new DeleteRoleOP(sessionId, wsUserRole);
	WebserviceExecutor.execute(deleteRole);

    }

    @WebMethod
    public List<WSUser> getUsers(String sessionId) throws WSException {

	GetUsersOP getUsers = new GetUsersOP(sessionId);
	WebserviceExecutor.execute(getUsers);
	return getUsers.getUsers();

    }

    @WebMethod
    public List<WSConfig> getConfigEntries(String sessionId) throws WSException {

	GetConfigEntriesOP getConfigEntries = new GetConfigEntriesOP(sessionId);
	WebserviceExecutor.execute(getConfigEntries);
	return getConfigEntries.getConfigEntries();

    }

    @WebMethod
    public void setConfigEntry(String sessionId, WSConfig wsConfig) throws WSException {

	SetConfigEntryOP setConfigEntry = new SetConfigEntryOP(sessionId, wsConfig);
	WebserviceExecutor.execute(setConfigEntry);

    }

    @WebMethod
    public List<WSLogEntry> getLogEntries(String sessionId, String user) throws SessionNotFoundException,
	    OperationException, PermissionDeniedException, LoginRequiredException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    authManager.requiresPermission(session, PermissionEnum.ADMIN_OPERATION);

	    AppLog logMgr = FACTORY.getLogManager(session, UserService.class);
	    List<LogEntry> logValues = logMgr.getLogEntries(user);
	    List<WSLogEntry> wsConfigList = WSConversionUtils.toWSLogList(logValues);
	    return wsConfigList;
	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public List<WSExportedSession> getSessionList(String sessionId) throws SessionNotFoundException,
	    OperationException, PermissionDeniedException, LoginRequiredException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    authManager.requiresPermission(session, PermissionEnum.ADMIN_OPERATION);
	    List<Session> sessions = sessionManager.getSessionList();

	    return WSConversionUtils.convertToSessionList(sessions);

	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public void logoutSession(String sessionId, String otherSessionId) throws SessionNotFoundException,
	    PermissionDeniedException, LoginRequiredException, OperationException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    authManager.requiresPermission(session, PermissionEnum.ADMIN_OPERATION);

	    sessionManager.logout(otherSessionId);

	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public List<WSStatistic> getStatistics(String sessionId, String filter) throws SessionNotFoundException,
	    OperationException, PermissionDeniedException, LoginRequiredException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    authManager.requiresPermission(session, PermissionEnum.ADMIN_OPERATION);

	    List<Statistic> statistics = StatisticsContainer.exportStatistics(filter);

	    return WSConversionUtils.convertToWSStatisticList(statistics);

	} finally {
	    FACTORY.releaseSession(session);
	}
    }
}
