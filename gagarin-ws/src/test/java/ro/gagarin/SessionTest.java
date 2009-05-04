package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import ro.gagarin.config.Config;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.exceptions.UserNotFoundException;
import ro.gagarin.hibernate.objects.DBUser;
import ro.gagarin.session.Session;
import ro.gagarin.ws.Authentication;

/**
 * Unit test for simple App.
 */
public class SessionTest {
	private Authentication authentication = new Authentication();
	private String username = "_User_" + System.currentTimeMillis();

	private Session session = new Session();

	// TODO: add test with null session for all WS methods

	@Test
	public void testSuccessLogin() throws UserNotFoundException, SessionNotFoundException,
			FieldRequiredException, UserAlreadyExistsException {

		UserDAO userManager = ModelFactory.getDAOManager().getUserDAO(session);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);
		ConfigurationManager cfgManager = ModelFactory.getConfigurationManager(session);

		DBUser user = new DBUser();
		user.setUsername("1" + username);
		user.setPassword("password1");
		user.setRole(roleManager.getRoleByName(cfgManager.getString(Config.ADMIN_ROLE_NAME)));
		userManager.createUser(user);
		ModelFactory.releaseSession(session);

		String session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.login(session, "1" + username, "password1", null);

		authentication.logout(session);
	}

	@Test
	public void testFailedLogin() throws SessionNotFoundException, FieldRequiredException,
			UserAlreadyExistsException {

		UserDAO userManager = ModelFactory.getDAOManager().getUserDAO(session);
		ConfigurationManager cfgManager = ModelFactory.getConfigurationManager(session);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);

		DBUser user = new DBUser();
		user.setUsername("2" + username);
		user.setPassword("password2");
		user.setRole(roleManager.getRoleByName(cfgManager.getString(Config.ADMIN_ROLE_NAME)));
		userManager.createUser(user);

		ModelFactory.releaseSession(session);

		String session = authentication.createSession(null, null);
		assertNotNull(session);

		try {
			authentication.login(session, "user2_", "password2", null);
			fail("The user does not exists");
		} catch (UserNotFoundException e) {
			// the exception was expected
		}
		try {
			authentication.login(session, "user2", "password2_", null);
			fail("The user and password does not match; thus authentication must fail");
		} catch (UserNotFoundException e) {
			// the exception was expected
		}
	}

	@Test
	public void testSessionDeletion() throws UserNotFoundException, FieldRequiredException,
			UserAlreadyExistsException {

		UserDAO userManager = ModelFactory.getDAOManager().getUserDAO(session);
		ConfigurationManager cfgManager = ModelFactory.getConfigurationManager(session);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);

		DBUser user = new DBUser();
		user.setUsername("3" + username);
		user.setPassword("password3");
		user.setRole(roleManager.getRoleByName(cfgManager.getString(Config.ADMIN_ROLE_NAME)));
		userManager.createUser(user);

		ModelFactory.releaseSession(session);

		String session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.logout(session);
		try {
			authentication.login(session, "3" + username, "password3", null);
			fail("The login must fail since the session was deleted");
		} catch (SessionNotFoundException e) {
			assertEquals("Wrong session ID returned by the exception", e.getSessionID(), session);
		}

	}

	@Test
	public void testSessionExpiration() throws InterruptedException {

		ConfigurationManager cfgManager = ModelFactory.getConfigurationManager(session);
		cfgManager.setConfigValue(Config.USER_SESSION_TIMEOUT, "100");

		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.createSession(null, null);
		assertNotNull(session);
		assertEquals("We just set the timeout to 100", session.getSessionTimeout(), 100);

		try {
			session = sessionManager.acquireSession(session.getSessionString());
		} catch (SessionNotFoundException e) {
			fail("The session should be active");
		}
		assertNotNull(session);

		Thread.sleep(110);
		try {
			session = sessionManager.acquireSession(session.getSessionString());
			fail("The session must be expired at this time");
		} catch (SessionNotFoundException e) {
			// expected exception
		}

		ModelFactory.releaseSession(session);
	}
}
