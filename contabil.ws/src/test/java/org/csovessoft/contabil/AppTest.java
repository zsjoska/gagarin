package org.csovessoft.contabil;

import static org.junit.Assert.assertNotNull;

import org.csovessoft.contabil.user.User;
import org.csovessoft.contabil.user.UserManager;
import org.csovessoft.contabil.ws.Authentication;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	@Test
	public void testLogin() {

		UserManager userManager = ModelFactory.getUserManager();

		User user = new User();
		userManager.createUser(user);

		Authentication authentication = new Authentication();
		String session = authentication.login(user.getUsername(), user
				.getPassword(), null);
		assertNotNull(session);
	}
}
