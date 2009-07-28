package ro.gagarin;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.UserService;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;

/**
 * Unit test for simple App.
 */
public class UserServiceTest {
	private static Authentication authentication = new Authentication();
	private static String username = "_User_" + System.currentTimeMillis();
	private static String session;

	@BeforeClass
	public static void startup() throws SessionNotFoundException, ItemNotFoundException,
			OperationException, DataConstraintException {
		session = authentication.createSession(null, null);
		authentication.login(session, "admin", "password", null);
	}

	@Test
	public void testCreateUser() throws SessionNotFoundException, ItemNotFoundException,
			PermissionDeniedException, DataConstraintException, OperationException {

		UserService userService = new UserService();

		List<WSUserRole> roles = userService.getRoleList(session);

		WSUser user = new WSUser();
		user.setUsername(username);
		user.setPassword("password");
		user.setRole(roles.get(0));

		userService.createUser(session, user);
	}

	@Test
	public void testCreateRole() throws SessionNotFoundException, PermissionDeniedException,
			OperationException, ItemNotFoundException, DataConstraintException {
		UserService userService = new UserService();

		WSUserPermission[] perms = new WSUserPermission[] {
				new WSUserPermission(PermissionEnum.CREATE_USER.name()),
				new WSUserPermission(PermissionEnum.CREATE_ROLE.name()) };

		UserRole role = userService.createRoleWithPermissions(session, "WONDER_ROLE", perms);
		List<WSUserPermission> rolePermissions = userService.getRolePermissions(session,
				new WSUserRole("WONDER_ROLE"));
		try {
			for (WSUserPermission perm : perms) {
				WSUserPermission found = null;
				for (WSUserPermission p : rolePermissions) {
					if (perm.getPermissionName().equalsIgnoreCase(p.getPermissionName())) {
						found = p;
					}
				}
				if (found == null) {
					fail(perm.getPermissionName() + " was not found");
				}

			}
		} finally {
			userService.deleteRole(session, role);
		}
	}

	@Test
	public void testListUsers() throws OperationException, SessionNotFoundException,
			PermissionDeniedException {

		UserService userService = new UserService();

		List<WSUser> users = userService.getUsers(session);
		for (WSUser wsUser : users) {
			System.err.println(wsUser.getUsername());
		}
	}
}
