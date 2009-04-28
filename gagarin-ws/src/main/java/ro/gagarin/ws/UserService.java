package ro.gagarin.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.ModelFactory;
import ro.gagarin.RoleManager;
import ro.gagarin.SessionManager;
import ro.gagarin.UserManager;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

@WebService
public class UserService {

	private static final transient Logger LOG = Logger.getLogger(UserService.class);

	@WebMethod
	public Long createUser(String sessionId, User user) throws SessionNotFoundException,
			FieldRequiredException, UserAlreadyExistsException, PermissionDeniedException {
		LOG.info("createUser " + user.getUsername());

		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.getSessionById(sessionId);
		UserManager userManager = ModelFactory.getUserManager(session);
		AuthorizationManager permissionManager = ModelFactory.getAuthorizationManager(session);

		try {
			if (session == null)
				throw new SessionNotFoundException(sessionId);

			// the session user must have CREATE_USER permission
			permissionManager.requiresPermission(session, PermissionEnum.CREATE_USER);

			// the created user's permission list must not exceed session user's
			// permissions
			permissionManager.checkUserRole(session, user);

			long userId = userManager.createUser(user);
			LOG.info("Created User " + user.getId() + ":" + user.getUsername() + "; session:"
					+ sessionId);
			return userId;
		} finally {
			ModelFactory.releaseManagers(session, userManager, permissionManager, sessionManager);
		}
	}

	@WebMethod
	public List<UserRole> getRoleList(String sessionId) throws SessionNotFoundException,
			PermissionDeniedException {
		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.getSessionById(sessionId);
		RoleManager roleManager = ModelFactory.getRoleManager(session);
		AuthorizationManager permissionManager = ModelFactory.getAuthorizationManager(session);

		try {
			if (session == null)
				throw new SessionNotFoundException(sessionId);

			// the session user must have LIST_ROLES permission
			permissionManager.requiresPermission(session, PermissionEnum.LIST_ROLES);

			return roleManager.getAllRoles();

		} finally {
			ModelFactory.releaseManagers(session, permissionManager, roleManager, sessionManager);
		}
	}

	@WebMethod
	public UserRole createRoleWithPermissions(String sessionId, String[] strings)
			throws SessionNotFoundException, PermissionDeniedException {
		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.getSessionById(sessionId);
		RoleManager roleManager = ModelFactory.getRoleManager(session);
		AuthorizationManager permissionManager = ModelFactory.getAuthorizationManager(session);

		try {
			if (session == null)
				throw new SessionNotFoundException(sessionId);

			// the session user must have LIST_ROLES permission
			permissionManager.requiresPermission(session, PermissionEnum.LIST_ROLES);

			return null;

		} finally {
			ModelFactory.releaseManagers(session, permissionManager, roleManager, sessionManager);
		}
	}

	@WebMethod
	public List<UserPermission> getAllPermissionList(String session) {
		// TODO Auto-generated method stub
		return null;
	}
}
