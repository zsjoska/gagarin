package org.csovessoft.contabil;

import org.csovessoft.wsclient.Authentication;
import org.csovessoft.wsclient.AuthenticationService;
import org.csovessoft.wsclient.FieldRequiredException_Exception;
import org.csovessoft.wsclient.Session;
import org.csovessoft.wsclient.SessionNotFoundException_Exception;
import org.csovessoft.wsclient.User;
import org.csovessoft.wsclient.UserAlreadyExistsException_Exception;
import org.csovessoft.wsclient.UserNotFoundException_Exception;
import org.csovessoft.wsclient.UserService;
import org.csovessoft.wsclient.UserServiceService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class UserServiceTest {

	private static Session session = null;
	private static Authentication api = null;

	@BeforeClass
	public static void setUp() throws SessionNotFoundException_Exception,
			UserNotFoundException_Exception {
		AuthenticationService service = new AuthenticationService();
		api = service.getAuthenticationPort();
		session = api.createSession(null, null);
		api.login(session.getId(), "admin", "test", null);
	}

	@AfterClass
	public static void shutDown() {
		api.logout(session.getId());
	}

	@Test
	public void createUser() throws FieldRequiredException_Exception,
			SessionNotFoundException_Exception, UserAlreadyExistsException_Exception {
		UserServiceService service = new UserServiceService();
		UserService userAPI = service.getUserServicePort();
		User user = new User();
		user.setUsername("wsUser1");
		user.setPassword("wspassword1");
		userAPI.createUser(session.getId(), user);
	}
}
