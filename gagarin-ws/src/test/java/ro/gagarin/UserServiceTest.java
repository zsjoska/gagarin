package ro.gagarin;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.hibernate.objects.DBUserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.UserService;
import ro.gagarin.ws.objects.WSUser;

/**
 * Unit test for simple App.
 */
public class UserServiceTest {
	private static Authentication authentication = new Authentication();
	private static String username = "_User_" + System.currentTimeMillis();
	private static String session;

	@BeforeClass
	public static void startup() throws SessionNotFoundException, ItemNotFoundException {
		session = authentication.createSession(null, null);
		authentication.login(session, "admin", "password", null);
	}

	@Test
	public void testCreateUser() throws SessionNotFoundException, ItemNotFoundException,
			FieldRequiredException, UserAlreadyExistsException, PermissionDeniedException {

		UserService userService = new UserService();

		List<UserRole> roles = userService.getRoleList(session);

		WSUser user = new WSUser();
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
