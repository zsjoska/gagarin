package org.csovessoft.contabil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.csovessoft.contabil.exceptions.SessionNotFoundException;
import org.csovessoft.contabil.exceptions.UserNotFoundException;
import org.csovessoft.contabil.session.Session;
import org.csovessoft.contabil.user.User;
import org.csovessoft.contabil.user.UserManager;
import org.csovessoft.contabil.ws.Authentication;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SessionTest {
	Authentication authentication = new Authentication();

	@Test
	public void testSuccessLogin() throws UserNotFoundException, SessionNotFoundException {

		UserManager userManager = ModelFactory.getUserManager();

		User user = new User();
		user.setUsername("user1");
		user.setPassword("password1");
		userManager.createUser(user);

		Session session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.login(session.getId(), "user1", "password1", null);

		authentication.logout(session.getId());
	}

	@Test
	public void testFailedLogin() throws SessionNotFoundException {

		UserManager userManager = ModelFactory.getUserManager();

		User user = new User();
		user.setUsername("user2");
		user.setPassword("password2");
		userManager.createUser(user);

		Session session = authentication.createSession(null, null);
		assertNotNull(session);

		try {
			authentication.login(session.getId(), "user2_", "password2", null);
			fail("The user does not exists");
		} catch (UserNotFoundException e) {
			// the exception was expected
		}
		try {
			authentication.login(session.getId(), "user2", "password2_", null);
			fail("The user and password does not match; thus authentication must fail");
		} catch (UserNotFoundException e) {
			// the exception was expected
		}
	}

	@Test
	public void testSessionDeletion() throws UserNotFoundException {

		UserManager userManager = ModelFactory.getUserManager();

		User user = new User();
		user.setUsername("user1");
		user.setPassword("password1");
		userManager.createUser(user);

		Session session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.logout(session.getId());
		try {
			authentication.login(session.getId(), "user1", "password1", null);
			fail("The login must fail since the session was deleted");
		} catch (SessionNotFoundException e) {
			assertEquals("Wrong session ID returned by the exception", e.getSessionID(), session
					.getId());
		}

	}
}
