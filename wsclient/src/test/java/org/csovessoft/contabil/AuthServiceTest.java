package org.csovessoft.contabil;

import org.csovessoft.wsclient.Authentication;
import org.csovessoft.wsclient.AuthenticationService;
import org.csovessoft.wsclient.Session;
import org.csovessoft.wsclient.SessionNotFoundException_Exception;
import org.csovessoft.wsclient.UserNotFoundException_Exception;
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
