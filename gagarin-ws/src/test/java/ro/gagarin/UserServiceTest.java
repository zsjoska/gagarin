package ro.gagarin;

import java.util.List;

import org.junit.Test;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.exceptions.UserNotFoundException;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.UserService;

/**
 * Unit test for simple App.
 */
public class UserServiceTest {
	private Authentication authentication = new Authentication();
	private String username = "_User_" + System.currentTimeMillis();

	@Test
	public void testCreateUser() throws SessionNotFoundException, UserNotFoundException,
			FieldRequiredException, UserAlreadyExistsException, PermissionDeniedException {
		String session = authentication.createSession(null, null);
		authentication.login(session, "admin", "password", null);

		UserService userService = new UserService();

		List<UserRole> roles = userService.getRoleList(session);

		User user = new User();
		user.setUsername("Admin2");
		user.setPassword("password");
		user.setRole(roles.get(0));

		userService.createUser(session, user);
	}
}
