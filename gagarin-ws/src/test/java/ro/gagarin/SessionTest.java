package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import ro.gagarin.config.Config;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.exceptions.UserNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;
import ro.gagarin.ws.Authentication;

/**
 * Unit test for simple App.
 */
public class SessionTest {
	private Authentication authentication = new Authentication();
	private String username = "_User_" + System.currentTimeMillis();

	private Session session = new Session();

	@Test
	public void testSuccessLogin() throws UserNotFoundException, SessionNotFoundException,
			FieldRequiredException, UserAlreadyExistsException {

		UserManager userManager = ModelFactory.getUserManager(session);

		User user = new User();
		user.setUsername("1" + username);
		user.setPassword("password1");
		userManager.createUser(user);

		Session session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.login(session.getId(), "1" + username, "password1", null);

		authentication.logout(session.getId());
	}

	@Test
	public void testFailedLogin() throws SessionNotFoundException, FieldRequiredException,
			UserAlreadyExistsException {

		UserManager userManager = ModelFactory.getUserManager(session);

		User user = new User();
		user.setUsername("2" + username);
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

		UserManager userManager = ModelFactory.getUserManager(session);

		User user = new User();
		user.setUsername("3" + username);
		user.setPassword("password3");
		userManager.createUser(user);

		Session session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.logout(session.getId());
		try {
			authentication.login(session.getId(), "3" + username, "password3", null);
			fail("The login must fail since the session was deleted");
		} catch (SessionNotFoundException e) {
			assertEquals("Wrong session ID returned by the exception", e.getSessionID(), session
					.getId());
		}

	}

	@Test
	public void testSessionExpiration() throws InterruptedException {

		ModelFactory.getConfigurationManager(session).setConfigValue(Config.USER_SESSION_TIMEOUT,
				"100");

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
