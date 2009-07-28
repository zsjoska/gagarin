package ro.gagarin.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.RoleDAO;
import ro.gagarin.SessionManager;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.ConversionUtils;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;
import ro.gagarin.ws.util.WSConversionUtils;

@WebService
public class UserService {

	private static final transient Logger LOG = Logger.getLogger(UserService.class);
	private static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();

	@WebMethod
	public Long createUser(String sessionId, WSUser user) throws SessionNotFoundException,
			PermissionDeniedException, ItemNotFoundException, DataConstraintException,
			OperationException {
		LOG.info("createUser " + user.getUsername());

		SessionManager sessionManager = FACTORY.getSessionManager();
		Session session = sessionManager.acquireSession(sessionId);
		UserDAO userManager = FACTORY.getDAOManager().getUserDAO(session);
		AuthorizationManager permissionManager = FACTORY.getAuthorizationManager(session);

		try {

			// the session user must have CREATE_USER permission
			permissionManager.requiresPermission(session, PermissionEnum.CREATE_USER);

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
			permissionManager.checkUserRole(session, user);

			long userId = userManager.createUser(user);
			LOG.info("Created User " + user.getId() + ":" + user.getUsername() + "; session:"
					+ sessionId);
			return userId;
		} finally {
			FACTORY.releaseSession(session);
		}
	}

	@WebMethod
	public List<WSUserRole> getRoleList(String sessionId) throws SessionNotFoundException,
			PermissionDeniedException, OperationException {

		SessionManager sessionManager = FACTORY.getSessionManager();
		Session session = sessionManager.acquireSession(sessionId);
		RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
		AuthorizationManager permissionManager = FACTORY.getAuthorizationManager(session);

		try {

			// the session user must have LIST_ROLES permission
			permissionManager.requiresPermission(session, PermissionEnum.LIST_ROLES);
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
	public WSUserRole createRoleWithPermissions(String sessionId, String roleName,
			WSUserPermission[] permissions) throws SessionNotFoundException,
			PermissionDeniedException, OperationException, ItemNotFoundException,
			DataConstraintException {

		SessionManager sessionManager = FACTORY.getSessionManager();
		Session session = sessionManager.acquireSession(sessionId);

		try {
			RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
			AuthorizationManager permissionManager = FACTORY.getAuthorizationManager(session);

			// the session user must have LIST_PERMISSIONS permission
			permissionManager.requiresPermission(session, PermissionEnum.LIST_PERMISSIONS);
			List<UserPermission> allPermissions = roleManager.getAllPermissions();
			List<UserPermission> matched;
			matched = ConversionUtils.matchPermissions(allPermissions, permissions);
			permissionManager.checkUserHasThePermissions(session, matched);

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
	public List<WSUserPermission> getAllPermissionList(String sessionId)
			throws SessionNotFoundException, OperationException, PermissionDeniedException {
		SessionManager sessionManager = FACTORY.getSessionManager();
		Session session = sessionManager.acquireSession(sessionId);

		try {
			RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
			AuthorizationManager permissionManager = FACTORY.getAuthorizationManager(session);

			// the session user must have LIST_PERMISSIONS permission
			permissionManager.requiresPermission(session, PermissionEnum.LIST_PERMISSIONS);

			List<UserPermission> allPermissions = roleManager.getAllPermissions();
			return WSConversionUtils.convertToWSPermissionList(allPermissions);

		} finally {
			FACTORY.releaseSession(session);
		}
	}

	@WebMethod
	public List<WSUserPermission> getRolePermissions(String sessionId, WSUserRole wsUserRole)
			throws SessionNotFoundException, OperationException, PermissionDeniedException {
		SessionManager sessionManager = FACTORY.getSessionManager();
		Session session = sessionManager.acquireSession(sessionId);

		try {
			RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
			AuthorizationManager permissionManager = FACTORY.getAuthorizationManager(session);

			// the session user must have LIST_PERMISSIONS permission
			permissionManager.requiresPermission(session, PermissionEnum.LIST_PERMISSIONS);

			Set<UserPermission> permissions = roleManager.getRolePermissions(wsUserRole);
			return WSConversionUtils.convertToWSPermissionList(permissions);

		} finally {
			FACTORY.releaseSession(session);
		}
	}

	@WebMethod
	public void deleteRole(String sessionId, UserRole role) throws OperationException,
			PermissionDeniedException, SessionNotFoundException {
		SessionManager sessionManager = FACTORY.getSessionManager();
		Session session = sessionManager.acquireSession(sessionId);

		try {
			RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
			AuthorizationManager permissionManager = FACTORY.getAuthorizationManager(session);

			// the session user must have LIST_PERMISSIONS permission
			permissionManager.requiresPermission(session, PermissionEnum.DELETE_ROLE);

			roleManager.deleteRole(role);

		} finally {
			FACTORY.releaseSession(session);
		}
	}
}
