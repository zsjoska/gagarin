package ro.gagarin;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class UserServiceTest {

	private static String session = null;
	private static Authentication api = null;

	@BeforeClass
	public static void setUp() throws SessionNotFoundException_Exception,
			ItemNotFoundException_Exception, OperationException_Exception {
		AuthenticationService service = new AuthenticationService();
		api = service.getAuthenticationPort();
		session = api.createSession(null, null);
		api.login(session, "admin", "password", null);
	}

	@AfterClass
	public static void shutDown() {
		api.logout(session);
	}

	@Test
	public void createUser() throws SessionNotFoundException_Exception,
			PermissionDeniedException_Exception, ItemNotFoundException_Exception,
			DataConstraintException_Exception, OperationException_Exception {
		UserServiceService service = new UserServiceService();
		UserService userAPI = service.getUserServicePort();
		WsUserRole role = new WsUserRole();
		role.setRoleName("ADMIN_ROLE");

		WsUser user = new WsUser();
		user.setUsername("wsUser1" + System.nanoTime());
		user.setPassword("wspassword1");
		user.setRole(role);
		userAPI.createUser(session, user);
	}
}
