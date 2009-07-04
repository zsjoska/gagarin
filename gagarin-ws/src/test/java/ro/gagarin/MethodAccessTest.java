package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.config.Config;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.UserService;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserRole;

/**
 * Unit test for simple App.
 */
public class MethodAccessTest {

	private static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();

	private static Authentication authentication = new Authentication();
	private static UserService userService = new UserService();

	private String session = null;

	private Session aDummySession;

	@Before
	public void setUp() throws SessionNotFoundException, ItemNotFoundException, OperationException {

		cleanup();

		aDummySession = new Session();
		aDummySession.setManagerFactory(FACTORY);
		ConfigurationManager cfgManager = FACTORY.getConfigurationManager(aDummySession);
		String adminUser = cfgManager.getString(Config.ADMIN_USER_NAME);
		String adminPassword = cfgManager.getString(Config.ADMIN_PASSWORD);

		this.session = authentication.createSession(null, null);
	}

	@After
	public void shutdown() throws OperationException {
		authentication.logout(session);
		FACTORY.releaseSession(aDummySession);
		cleanup();
	}

	private void cleanup() throws OperationException {

		Session cleanupSession = new Session();
		cleanupSession.setManagerFactory(FACTORY);

		UserDAO userDAO = FACTORY.getDAOManager().getUserDAO(cleanupSession);
		RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(cleanupSession);

		User user = userDAO.getUserByUsername("weakUser");
		if (user != null)
			userDAO.deleteUser(user);
		UserRole role = roleDAO.getRoleByName("weak");
		if (role != null)
			roleDAO.deleteRole(role);
		role = roleDAO.getRoleByName("stronger");
		if (role != null)
			roleDAO.deleteRole(role);
		FACTORY.releaseSession(cleanupSession);

	}

	@Test
	public void createUserAccess() throws ItemNotFoundException, SessionNotFoundException,
			DataConstraintException, OperationException {

		RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(aDummySession);
		UserDAO userDAO = FACTORY.getDAOManager().getUserDAO(aDummySession);

		// long id =
		// createUserRoleWithPermissions(aDummySession,"weakUser","weak" );

		List<UserPermission> allPermissions = roleDAO.getAllPermissions();
		assertTrue(allPermissions.size() > 3);
		WSUserRole role1 = new WSUserRole();
		role1.setRoleName("weak");
		roleDAO.createRole(role1);
		WSUser weakUser = new WSUser();
		weakUser.setUsername("weakUser");
		weakUser.setPassword("password");
		weakUser.setRole(role1);
		userDAO.createUser(weakUser);

		// have it committed so other sessions to have access to it
		FACTORY.releaseSession(aDummySession);
		// and recreate objects

		// TODO: it must be a bug allowing this
		// should not be allowed so simple to reuse a session
		roleDAO = FACTORY.getDAOManager().getRoleDAO(aDummySession);
		userDAO = FACTORY.getDAOManager().getUserDAO(aDummySession);

		authentication.login(session, "weakUser", "password", null);
		WSUser notCreated = new WSUser();
		notCreated.setUsername("any");
		notCreated.setPassword("any");
		notCreated.setRole(role1);
		try {
			userService.createUser(session, notCreated);
			fail("weakUser's role not enugh to create a user");
		} catch (PermissionDeniedException e) {
			// expected
		}

		roleDAO.assignPermissionToRole(role1, findPermission(PermissionEnum.CREATE_USER,
				allPermissions));
		WSUserRole role2 = new WSUserRole();
		role2.setRoleName("stronger");
		roleDAO.createRole(role2);
		roleDAO.assignPermissionToRole(role2, findPermission(PermissionEnum.LIST_ROLES,
				allPermissions));

		List<UserPermission> left = roleDAO.substractUsersRolePermissions(role2, role1);
		assertEquals(1, left.size());

		// have it committed so other sessions to have access to it
		FACTORY.releaseSession(aDummySession);
		// and recreate objects
		roleDAO = FACTORY.getDAOManager().getRoleDAO(aDummySession);
		userDAO = FACTORY.getDAOManager().getUserDAO(aDummySession);

		notCreated.setRole(role2);
		try {
			userService.createUser(session, notCreated);
			fail("weakUser could not create a user with LIST_ROLES");
		} catch (PermissionDeniedException e) {
			// expected
		}

		userDAO.deleteUser(weakUser);
		roleDAO.deleteRole(role1);
	}

	private UserPermission findPermission(PermissionEnum create_user,
			List<UserPermission> allPermissions) {
		for (UserPermission userPermission : allPermissions) {
			if (create_user.name().equals(userPermission.getPermissionName()))
				return userPermission;
		}
		fail("permission " + create_user.name() + " was not found");
		return null;
	}
}
