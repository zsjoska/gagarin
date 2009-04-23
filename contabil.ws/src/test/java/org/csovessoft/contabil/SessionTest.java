package org.csovessoft.contabil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.csovessoft.contabil.config.Config;
import org.csovessoft.contabil.exceptions.FieldRequiredException;
import org.csovessoft.contabil.exceptions.SessionNotFoundException;
import org.csovessoft.contabil.exceptions.UserAlreadyExistsException;
import org.csovessoft.contabil.exceptions.UserNotFoundException;
import org.csovessoft.contabil.session.Session;
import org.csovessoft.contabil.user.User;
import org.csovessoft.contabil.ws.Authentication;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SessionTest {
	Authentication authentication = new Authentication();

	@Test
	public void testSuccessLogin() throws UserNotFoundException, SessionNotFoundException,
			FieldRequiredException, UserAlreadyExistsException {

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
	public void testFailedLogin() throws SessionNotFoundException, FieldRequiredException,
			UserAlreadyExistsException {

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
	public void testSessionDeletion() throws UserNotFoundException, FieldRequiredException,
			UserAlreadyExistsException {

		UserManager userManager = ModelFactory.getUserManager();

		User user = new User();
		user.setUsername("user3");
		user.setPassword("password3");
		userManager.createUser(user);

		Session session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.logout(session.getId());
		try {
			authentication.login(session.getId(), "user3", "password3", null);
			fail("The login must fail since the session was deleted");
		} catch (SessionNotFoundException e) {
			assertEquals("Wrong session ID returned by the exception", e.getSessionID(), session
					.getId());
		}

	}

	@Test
	public void testSessionExpiration() throws InterruptedException {

		ModelFactory.getConfigurationManager().setConfigValue(Config.USER_SESSION_TIMEOUT, "100");

		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.createSession(null, null);
		assertNotNull(session);
		assertEquals("We just set the timeout to 100", session.getSessionTimeout(), 100);

		session = sessionManager.getSessionById(session.getId());
		assertNotNull(session);

		Thread.sleep(101);
		session = sessionManager.getSessionById(session.getId());
		assertNull("The session must be expired at this time", session);
	}
}
