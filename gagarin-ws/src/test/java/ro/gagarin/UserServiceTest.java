package ro.gagarin;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
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
	public static void startup() throws SessionNotFoundException, ItemNotFoundException {
		session = authentication.createSession(null, null);
		authentication.login(session, "admin", "password", null);
	}

	@Test
	public void testCreateUser() throws SessionNotFoundException, ItemNotFoundException,
			FieldRequiredException, ItemExistsException, PermissionDeniedException {

		UserService userService = new UserService();

		List<WSUserRole> roles = userService.getRoleList(session);

		WSUser user = new WSUser();
		user.setUsername(username);
		user.setPassword("password");
		user.setRole(roles.get(0));

		userService.createUser(session, user);
	}

	@Test
	public void testCreateRole() throws SessionNotFoundException, PermissionDeniedException {
		UserService userService = new UserService();
		List<WSUserPermission> list = userService.getAllPermissionList(session);
		UserRole role = userService.createRoleWithPermissions(session,
				new String[] { "CREATE_USER" });
	}
}
