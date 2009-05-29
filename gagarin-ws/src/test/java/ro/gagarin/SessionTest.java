package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import ro.gagarin.application.objects.AppUser;
import ro.gagarin.config.Config;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.jdbc.objects.DBUser;
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
	public void testSuccessLogin() throws ItemNotFoundException, SessionNotFoundException,
			FieldRequiredException, ItemExistsException {

		ManagerFactory factory = BasicManagerFactory.getInstance();

		UserDAO userManager = factory.getDAOManager().getUserDAO(session);
		RoleDAO roleManager = factory.getDAOManager().getRoleDAO(session);
		ConfigurationManager cfgManager = factory.getConfigurationManager(session);

		AppUser user = new AppUser();
		user.setUsername("1" + username);
		user.setPassword("password1");
		user.setRole(roleManager.getRoleByName(cfgManager.getString(Config.ADMIN_ROLE_NAME)));
		userManager.createUser(user);
		factory.releaseSession(session);

		String session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.login(session, "1" + username, "password1", null);

		authentication.logout(session);
	}

	@Test
	public void testFailedLogin() throws SessionNotFoundException, FieldRequiredException,
			ItemExistsException, ItemNotFoundException {

		ManagerFactory factory = BasicManagerFactory.getInstance();

		UserDAO userManager = factory.getDAOManager().getUserDAO(session);
		ConfigurationManager cfgManager = factory.getConfigurationManager(session);
		RoleDAO roleManager = factory.getDAOManager().getRoleDAO(session);

		DBUser user = new DBUser();
		user.setUsername("2" + username);
		user.setPassword("password2");
		user.setRole(roleManager.getRoleByName(cfgManager.getString(Config.ADMIN_ROLE_NAME)));
		userManager.createUser(user);

		factory.releaseSession(session);

		String session = authentication.createSession(null, null);
		assertNotNull(session);

		try {
			authentication.login(session, "user2_", "password2", null);
			fail("The user does not exists");
		} catch (ItemNotFoundException e) {
			// the exception was expected
		}
		try {
			authentication.login(session, "user2", "password2_", null);
			fail("The user and password does not match; thus authentication must fail");
		} catch (ItemNotFoundException e) {
			// the exception was expected
		}
	}

	@Test
	public void testSessionDeletion() throws ItemNotFoundException, FieldRequiredException,
			ItemExistsException {
		ManagerFactory factory = BasicManagerFactory.getInstance();

		UserDAO userManager = factory.getDAOManager().getUserDAO(session);
		ConfigurationManager cfgManager = factory.getConfigurationManager(session);
		RoleDAO roleManager = factory.getDAOManager().getRoleDAO(session);

		DBUser user = new DBUser();
		user.setUsername("3" + username);
		user.setPassword("password3");
		user.setRole(roleManager.getRoleByName(cfgManager.getString(Config.ADMIN_ROLE_NAME)));
		userManager.createUser(user);

		factory.releaseSession(session);

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

		ManagerFactory factory = BasicManagerFactory.getInstance();

		ConfigurationManager cfgManager = factory.getConfigurationManager(session);
		cfgManager.setConfigValue(Config.USER_SESSION_TIMEOUT, "100");

		SessionManager sessionManager = factory.getSessionManager();
		Session session = sessionManager.createSession(null, null, BasicManagerFactory.getInstance());
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

		factory.releaseSession(session);
	}
}