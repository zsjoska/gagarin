package org.csovessoft.contabil;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.csovessoft.contabil.user.User;
import org.csovessoft.contabil.user.UserManager;
import org.csovessoft.contabil.ws.Authentication;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SessionTest {

	@Test
	public void testSuccessLogin() {

		UserManager userManager = ModelFactory.getUserManager();

		User user = new User();
		user.setUsername("user1");
		user.setPassword("password1");
		userManager.createUser(user);

		Authentication authentication = new Authentication();
		String session = authentication.login(user.getUsername(), user
				.getPassword(), null);
		assertNotNull(session);
	}

	@Test
	public void testFailedLogin() {

		UserManager userManager = ModelFactory.getUserManager();

		User user = new User();
		user.setUsername("user2");
		user.setPassword("password2");
		userManager.createUser(user);

		Authentication authentication = new Authentication();
		String session = authentication.login(user.getUsername(), user
				.getPassword()
				+ "_", null);
		assertNull(session);
	}
}
