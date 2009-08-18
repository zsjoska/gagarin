package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.application.objects.AppUser;
import ro.gagarin.config.Config;
import ro.gagarin.config.DBConfigManager;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.session.Session;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.Authentication;

/**
 * Unit test for simple App.
 */
public class SessionTest {
	private static final transient Logger LOG = Logger
			.getLogger(SessionTest.class);

	private static final ManagerFactory FACTORY = BasicManagerFactory
			.getInstance();

	private static final long LOCAL_DB_CONFIG_CHECK_PERIOD = 100;

	private Authentication authentication = new Authentication();
	private String username = "_User_" + System.currentTimeMillis();

	private Session session = new Session();

	// TODO: add test with null session for all WS methods

	@BeforeClass
	public static void startup() {
		DBConfigManager.getInstance().configChanged(
				Config.DB_CONFIG_CHECK_PERIOD,
				"" + LOCAL_DB_CONFIG_CHECK_PERIOD);
	}

	@AfterClass
	public static void shutdown() {
		DBConfigManager dbConfig = DBConfigManager.getInstance();
		dbConfig.configChanged(Config.DB_CONFIG_CHECK_PERIOD, ""
				+ dbConfig.getLong(Config.DB_CONFIG_CHECK_PERIOD));
	}

	@Test
	public void testSuccessLogin() throws SessionNotFoundException,
			DataConstraintException, ItemNotFoundException, OperationException {

		session = TUtil.createTestSession();

		UserDAO userManager = FACTORY.getDAOManager().getUserDAO(session);
		RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
		ConfigurationManager cfgManager = FACTORY.getConfigurationManager();

		List<UserRole> allRoles = roleManager.getAllRoles();
		LOG.debug("Roles in system:");
		for (UserRole userRole : allRoles) {
			LOG.debug(userRole.getRoleName());
		}
		LOG.debug("End roles listing");

		AppUser user = new AppUser();
		user.setUsername("1" + username);
		user.setPassword("password1");
		user.setRole(roleManager.getRoleByName(cfgManager
				.getString(Config.ADMIN_ROLE_NAME)));
		userManager.createUser(user);
		FACTORY.releaseSession(session);

		String session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.login(session, "1" + username, "password1", null);

		authentication.logout(session);
	}

	@Test
	public void testFailedLogin() throws SessionNotFoundException,
			ItemNotFoundException, DataConstraintException, OperationException {

		session = TUtil.createTestSession();

		UserDAO userManager = FACTORY.getDAOManager().getUserDAO(session);
		ConfigurationManager cfgManager = FACTORY.getConfigurationManager();
		RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

		DBUser user = new DBUser();
		user.setUsername("2" + username);
		user.setPassword("password2");
		user.setRole(roleManager.getRoleByName(cfgManager
				.getString(Config.ADMIN_ROLE_NAME)));
		userManager.createUser(user);

		FACTORY.releaseSession(session);

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
	public void testSessionDeletion() throws ItemNotFoundException,
			DataConstraintException, OperationException {
		session = TUtil.createTestSession();

		UserDAO userManager = FACTORY.getDAOManager().getUserDAO(session);
		ConfigurationManager cfgManager = FACTORY.getConfigurationManager();
		RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

		DBUser user = new DBUser();
		user.setUsername("3" + username);
		user.setPassword("password3");
		user.setRole(roleManager.getRoleByName(cfgManager
				.getString(Config.ADMIN_ROLE_NAME)));
		userManager.createUser(user);

		FACTORY.releaseSession(session);

		String session = authentication.createSession(null, null);
		assertNotNull(session);

		authentication.logout(session);
		try {
			authentication.login(session, "3" + username, "password3", null);
			fail("The login must fail since the session was deleted");
		} catch (SessionNotFoundException e) {
			assertEquals("Wrong session ID returned by the exception", e
					.getSessionID(), session);
		}

	}

	@Test
	public void testSessionExpiration() throws InterruptedException,
			SessionNotFoundException, OperationException {

		session = FACTORY.getSessionManager()
				.createSession(null, null, FACTORY);
		FACTORY.getSessionManager().acquireSession(session.getSessionString());

		ConfigurationManager cfgManager = FACTORY.getConfigurationManager();
		cfgManager.setConfigValue(session, Config.USER_SESSION_TIMEOUT, "100");
		FACTORY.releaseSession(session);

		LOG.info("Waiting DB config import for " + LOCAL_DB_CONFIG_CHECK_PERIOD);
		Thread.sleep(LOCAL_DB_CONFIG_CHECK_PERIOD);

		SessionManager sessionManager = FACTORY.getSessionManager();
		Session session = FACTORY.getSessionManager().createSession(null, null,
				FACTORY);
		assertNotNull(session);
		assertEquals("We just set the timeout to 100", session
				.getSessionTimeout(), 100);

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

		FACTORY.releaseSession(session);
	}
}
