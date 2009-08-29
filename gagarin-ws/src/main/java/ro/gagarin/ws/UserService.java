package ro.gagarin.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ConfigurationManager;
import ro.gagarin.ManagerFactory;
import ro.gagarin.RoleDAO;
import ro.gagarin.SessionManager;
import ro.gagarin.UserDAO;
import ro.gagarin.config.ConfigEntry;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.log.LogEntry;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.ConversionUtils;
import ro.gagarin.utils.Statistic;
import ro.gagarin.utils.StatisticsContainer;
import ro.gagarin.ws.objects.WSConfig;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.objects.WSStatistic;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;
import ro.gagarin.ws.util.WSConversionUtils;

@WebService
public class UserService {

    private static final transient Logger LOG = Logger.getLogger(UserService.class);
    private static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    @WebMethod
    public Long createUser(String sessionId, WSUser user) throws SessionNotFoundException, PermissionDeniedException,
	    ItemNotFoundException, DataConstraintException, OperationException, LoginRequiredException {
	LOG.info("createUser " + user.getUsername());

	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    // the session user must have CREATE_USER permission
	    authManager.requiresPermission(session, PermissionEnum.CREATE_USER);

	    UserDAO userManager = FACTORY.getDAOManager().getUserDAO(session);

	    // TODO: what for this rollback... no change so far
	    // check user fields
	    if (user.getRole() == null) {
		userManager.markRollback();
		throw new FieldRequiredException("ROLE", User.class);
	    }

	    UserRole role = user.getRole();
	    if (role.getId() == null && role.getRoleName() != null) {
		RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
		role = roleDAO.getRoleByName(role.getRoleName());
		if (role == null) {
		    userManager.markRollback();
		    throw new ItemNotFoundException(UserRole.class, user.getRole().getRoleName());
		}
		user.setRole(role);
	    }

	    // the created user's permission list must not exceed session user's
	    // permissions
	    authManager.checkUserRole(session, user);

	    long userId = userManager.createUser(user);
	    LOG.info("Created User " + user.getId() + ":" + user.getUsername() + "; session:" + sessionId);
	    return userId;
	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public List<WSUserRole> getRoleList(String sessionId) throws SessionNotFoundException, PermissionDeniedException,
	    OperationException, LoginRequiredException {

	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    // the session user must have LIST_ROLES permission
	    authManager.requiresPermission(session, PermissionEnum.LIST_ROLES);

	    RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
	    List<UserRole> allRoles = roleManager.getAllRoles();
	    List<WSUserRole> convRoles = new ArrayList<WSUserRole>();
	    for (UserRole userRole : allRoles) {
		convRoles.add(new WSUserRole(userRole));
	    }
	    return convRoles;

	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public WSUserRole createRoleWithPermissions(String sessionId, String roleName, WSUserPermission[] permissions)
	    throws SessionNotFoundException, PermissionDeniedException, OperationException, ItemNotFoundException,
	    DataConstraintException, LoginRequiredException {

	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

	    LOG.debug("Create role:" + roleName + " with permissions " + Arrays.toString(permissions));

	    // the session user must have LIST_PERMISSIONS permission
	    authManager.requiresPermission(session, PermissionEnum.LIST_PERMISSIONS);
	    List<UserPermission> allPermissions = roleManager.getAllPermissions();
	    List<UserPermission> matched;
	    matched = ConversionUtils.matchPermissions(allPermissions, permissions);
	    authManager.checkUserHasThePermissions(session, matched);

	    WSUserRole role = new WSUserRole();
	    role.setRoleName(roleName);
	    role.setId(roleManager.createRole(role));

	    for (UserPermission userPermission : matched) {
		roleManager.assignPermissionToRole(role, userPermission);
	    }

	    return role;

	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public List<WSUserPermission> getAllPermissionList(String sessionId) throws SessionNotFoundException,
	    OperationException, PermissionDeniedException, LoginRequiredException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

	    // the session user must have LIST_PERMISSIONS permission
	    authManager.requiresPermission(session, PermissionEnum.LIST_PERMISSIONS);

	    List<UserPermission> allPermissions = roleManager.getAllPermissions();
	    return WSConversionUtils.convertToWSPermissionList(allPermissions);

	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public List<WSUserPermission> getRolePermissions(String sessionId, WSUserRole wsUserRole)
	    throws SessionNotFoundException, OperationException, PermissionDeniedException, LoginRequiredException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

	    // the session user must have LIST_PERMISSIONS permission
	    authManager.requiresPermission(session, PermissionEnum.LIST_PERMISSIONS);

	    Set<UserPermission> permissions = roleManager.getRolePermissions(wsUserRole);
	    return WSConversionUtils.convertToWSPermissionList(permissions);

	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public void deleteRole(String sessionId, WSUserRole role) throws OperationException, PermissionDeniedException,
	    SessionNotFoundException, LoginRequiredException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

	    // the session user must have LIST_PERMISSIONS permission
	    authManager.requiresPermission(session, PermissionEnum.DELETE_ROLE);

	    roleManager.deleteRole(role);

	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public List<WSUser> getUsers(String sessionId) throws OperationException, SessionNotFoundException,
	    PermissionDeniedException, LoginRequiredException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    UserDAO userDAO = FACTORY.getDAOManager().getUserDAO(session);

	    // the session user must have LIST_USERS permission
	    authManager.requiresPermission(session, PermissionEnum.LIST_USERS);

	    List<User> allUsers = userDAO.getAllUsers();
	    return WSConversionUtils.convertToWSUserList(allUsers);

	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public List<WSConfig> getConfigEntries(String sessionId) throws SessionNotFoundException, OperationException,
	    PermissionDeniedException, LoginRequiredException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    authManager.requiresPermission(session, PermissionEnum.ADMIN_OPERATION);

	    ConfigurationManager cfgMgr = FACTORY.getConfigurationManager();
	    List<ConfigEntry> configValues = cfgMgr.getConfigValues();
	    List<WSConfig> wsConfigList = WSConversionUtils.toWSConfigList(configValues);
	    return wsConfigList;
	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public void setConfigEntry(String sessionId, WSConfig wsConfig) throws SessionNotFoundException,
	    OperationException, PermissionDeniedException, LoginRequiredException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);

	try {
	    AuthorizationManager authManager = FACTORY.getAuthorizationManager(session);

	    // check real login
	    authManager.requireLogin(session);

	    authManager.requiresPermission(session, PermissionEnum.ADMIN_OPERATION);

	    ConfigurationManager cfgMgr = FACTORY.getConfigurationManager();
	    cfgMgr.setConfigValue(session, wsConfig);
	} finally {
	    FACTORY.releaseSession(session);
	}
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
