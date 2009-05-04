package ro.gagarin;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.exceptions.UserNotFoundException;
import ro.gagarin.hibernate.objects.DBUser;
import ro.gagarin.hibernate.objects.DBUserPermission;
import ro.gagarin.hibernate.objects.DBUserRole;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.UserService;

/**
 * Unit test for simple App.
 */
public class UserServiceTest {
	private static Authentication authentication = new Authentication();
	private static String username = "_User_" + System.currentTimeMillis();
	private static String session;

	@BeforeClass
	public static void startup() throws SessionNotFoundException, UserNotFoundException {
		session = authentication.createSession(null, null);
		authentication.login(session, "admin", "password", null);
	}

	@Test
	public void testCreateUser() throws SessionNotFoundException, UserNotFoundException,
			FieldRequiredException, UserAlreadyExistsException, PermissionDeniedException {

		UserService userService = new UserService();

		List<DBUserRole> roles = userService.getRoleList(session);

		DBUser user = new DBUser();
		user.setUsername(username);
		user.setPassword("password");
		user.setRole(roles.get(0));

		userService.createUser(session, user);
	}

	@Test
	public void testCreateRole() throws SessionNotFoundException, PermissionDeniedException {
		UserService userService = new UserService();
		List<DBUserPermission> list = userService.getAllPermissionList(session);
		UserRole role = userService.createRoleWithPermissions(session,
				new String[] { "CREATE_USER" });
	}
}
