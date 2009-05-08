package ro.gagarin.ws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.ModelFactory;
import ro.gagarin.RoleDAO;
import ro.gagarin.SessionManager;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;

@WebService
public class UserService {

	private static final transient Logger LOG = Logger.getLogger(UserService.class);

	@WebMethod
	public Long createUser(String sessionId, WSUser user) throws SessionNotFoundException,
			FieldRequiredException, UserAlreadyExistsException, PermissionDeniedException,
			ItemNotFoundException {
		LOG.info("createUser " + user.getUsername());

		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.acquireSession(sessionId);
		UserDAO userManager = ModelFactory.getDAOManager().getUserDAO(session);
		AuthorizationManager permissionManager = ModelFactory.getAuthorizationManager(session);

		try {

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
			ModelFactory.releaseSession(session);
		}
	}

	@WebMethod
	public List<WSUserRole> getRoleList(String sessionId) throws SessionNotFoundException,
			PermissionDeniedException {
		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.acquireSession(sessionId);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);
		AuthorizationManager permissionManager = ModelFactory.getAuthorizationManager(session);

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
			ModelFactory.releaseSession(session);
		}
	}

	@WebMethod
	public WSUserRole createRoleWithPermissions(String sessionId, String[] strings)
			throws SessionNotFoundException, PermissionDeniedException {
		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.acquireSession(sessionId);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);
		AuthorizationManager permissionManager = ModelFactory.getAuthorizationManager(session);

		try {

			// the session user must have LIST_ROLES permission
			permissionManager.requiresPermission(session, PermissionEnum.LIST_ROLES);

			return null;

		} finally {
			ModelFactory.releaseSession(session);
		}
	}

	@WebMethod
	public List<WSUserPermission> getAllPermissionList(String session) {
		// TODO Auto-generated method stub
		return null;
	}
}
