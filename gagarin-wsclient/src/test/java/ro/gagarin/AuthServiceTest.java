package ro.gagarin;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AuthServiceTest {

	@Test
	public void testSessionCreateLoginLogout() throws SessionNotFoundException_Exception,
			UserNotFoundException_Exception {
		AuthenticationService service = new AuthenticationService();
		Authentication api = service.getAuthenticationPort();
		Session session = api.createSession(null, null);
		api.login(session.getId(), "admin", "test", null);
		api.logout(session.getId());
	}
}
