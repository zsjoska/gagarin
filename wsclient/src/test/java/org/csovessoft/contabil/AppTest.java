package org.csovessoft.contabil;

import org.csovessoft.wsclient.Authentication;
import org.csovessoft.wsclient.AuthenticationService;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	@Test
	public void testApp() {
		AuthenticationService service = new AuthenticationService();
		Authentication port = service.getAuthenticationPort();
	}
}
