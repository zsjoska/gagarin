package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.config.Config;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
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
	private static Authentication authentication = new Authentication();
	private static UserService userService = new UserService();

	private String session = null;

	private Session aDummySession;

	@Before
	public void setUp() throws SessionNotFoundException, ItemNotFoundException {

		aDummySession = new Session();
		ConfigurationManager cfgManager = ModelFactory.getConfigurationManager(aDummySession);
		String adminUser = cfgManager.getString(Config.ADMIN_USER_NAME);
		String adminPassword = cfgManager.getString(Config.ADMIN_PASSWORD);

		this.session = authentication.createSession(null, null);
	}

	@After
	public void shutdown() {
		authentication.logout(session);
		cleanup();
		ModelFactory.releaseSession(aDummySession);
	}

	private void cleanup() {

		UserDAO userDAO = ModelFactory.getDAOManager().getUserDAO(aDummySession);
		List<User> allUsers = userDAO.getAllUsers();
		for (User user : allUsers) {
			userDAO.deleteUser(user);
		}

		RoleDAO roleDAO = ModelFactory.getDAOManager().getRoleDAO(aDummySession);
		List<UserRole> allRoles = roleDAO.getAllRoles();
		for (UserRole userRole : allRoles) {
			roleDAO.deleteRole(userRole);
		}

		List<UserPermission> allPermissions = roleDAO.getAllPermissions();
		for (UserPermission userPermission : allPermissions) {
			roleDAO.deletePermission(userPermission);
		}

	}

	@Test
	public void createUserAccess() throws FieldRequiredException, UserAlreadyExistsException,
			ItemNotFoundException, SessionNotFoundException {
		RoleDAO roleDAO = ModelFactory.getDAOManager().getRoleDAO(aDummySession);
		UserDAO userDAO = ModelFactory.getDAOManager().getUserDAO(aDummySession);
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
		ModelFactory.releaseSession(aDummySession);
		// and recreate objects
		roleDAO = ModelFactory.getDAOManager().getRoleDAO(aDummySession);
		userDAO = ModelFactory.getDAOManager().getUserDAO(aDummySession);

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
		ModelFactory.releaseSession(aDummySession);
		// and recreate objects
		roleDAO = ModelFactory.getDAOManager().getRoleDAO(aDummySession);
		userDAO = ModelFactory.getDAOManager().getUserDAO(aDummySession);

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
