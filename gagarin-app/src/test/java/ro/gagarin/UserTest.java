package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.application.objects.AppUser;
import ro.gagarin.config.Config;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestUser;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

/**
 * Unit test for simple App.
 */
public class UserTest {
	private String username = "User_" + System.nanoTime();

	private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

	private Session session = null;

	@Before
	public void init() {
		this.session = TUtil.createTestSession();
	}

	@After
	public void close() {
		FACTORY.releaseSession(session);
	}

	private ConfigurationManager configManager = FACTORY.getConfigurationManager(session);

	@Test
	public void getUserByNameInexistent() throws OperationException {
		UserDAO usrManager = FACTORY.getDAOManager().getUserDAO(session);
		User user = usrManager.getUserByUsername(username);
		assertNull("The user could not exists", user);
	}

	@Test
	public void createUser() throws ItemNotFoundException, DataConstraintException,
			OperationException {

		UserDAO usrManager = FACTORY.getDAOManager().getUserDAO(session);
		RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

		UserRole adminRole = roleManager.getRoleByName(configManager
				.getString(Config.ADMIN_ROLE_NAME));
		assertNotNull("this test requires application setup", adminRole);

		AppUser user = new AppUser();
		user.setName("Name Of User");
		user.setUsername(username);
		user.setPassword("password" + username);
		user.setRole(adminRole);
		usrManager.createUser(user);
		User user2 = usrManager.getUserByUsername(username);

		assertNotNull("User was not found", user2);
		assertEquals("id does not match", user.getId(), user2.getId());
		assertEquals("name does not match", user.getName(), user2.getName());
		assertEquals("username does not match", user.getUsername(), user2.getUsername());
		assertNotNull("The role field must be filled by queries", user2.getRole());

		usrManager.deleteUser(user);
		assertNull("We just deleted the user; must not exists", usrManager
				.getUserByUsername(username));
	}

	@Test
	public void usersWiththeSameID() throws ItemNotFoundException, DataConstraintException,
			OperationException {

		Session brokenSession = TUtil.createTestSession();

		UserDAO usrManager = FACTORY.getDAOManager().getUserDAO(brokenSession);
		RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(brokenSession);

		UserRole adminRole = roleManager.getRoleByName(configManager
				.getString(Config.ADMIN_ROLE_NAME));

		ATestUser user1 = new ATestUser();
		user1.setUsername("UserName1");
		user1.setPassword("password");
		user1.setRole(adminRole);

		ATestUser user2 = new ATestUser();
		user2.setUsername("UserName2");
		user2.setPassword("password");
		user2.setRole(adminRole);

		user2.setId(user1.getId());

		usrManager.createUser(user1);

		assertNotNull(usrManager.getUserByUsername("UserName1"));

		try {
			usrManager.createUser(user2);
			fail("the userid is the same; thus this item must not be created");
		} catch (ItemExistsException e) {
			assertEquals("Wrong field info", "ID", e.getFieldName());
			assertEquals("Wrong class info", "User", e.getClassName());
		} finally {
			FACTORY.releaseSession(brokenSession);
		}

		usrManager = FACTORY.getDAOManager().getUserDAO(session);
		assertNull("Transaction rolback test", usrManager.getUserByUsername("UserName1"));

	}

	@Test
	public void usersWiththeSameUsername() throws ItemNotFoundException, DataConstraintException,
			OperationException {

		UserDAO usrManager = FACTORY.getDAOManager().getUserDAO(session);
		RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

		UserRole adminRole = roleManager.getRoleByName(configManager
				.getString(Config.ADMIN_ROLE_NAME));

		ATestUser user1 = new ATestUser();
		user1.setUsername("UserName2");
		user1.setPassword("password");
		user1.setRole(adminRole);

		ATestUser user2 = new ATestUser();
		user2.setUsername("UserName2");
		user2.setPassword("password");
		user2.setRole(adminRole);

		usrManager.createUser(user1);

		assertNotNull(usrManager.getUserByUsername("UserName2"));

		try {
			usrManager.createUser(user2);
			fail("the username is the same; thus this item must not be created");
		} catch (ItemExistsException e) {
			assertEquals("Wrong field info", "USERNAME", e.getFieldName());
			assertEquals("Wrong class info", "User", e.getClassName());
		} finally {
			FACTORY.releaseSession(session);
		}

		session = TUtil.createTestSession();
		usrManager = FACTORY.getDAOManager().getUserDAO(session);
		assertNull("Transaction rolback test", usrManager.getUserByUsername("UserName2"));

	}

	@Test
	public void usersWithoutUsername() throws ItemNotFoundException, DataConstraintException,
			OperationException {

		Session brokenSession = TUtil.createTestSession();

		UserDAO usrManager = FACTORY.getDAOManager().getUserDAO(brokenSession);
		RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(brokenSession);

		UserRole adminRole = roleManager.getRoleByName(configManager
				.getString(Config.ADMIN_ROLE_NAME));

		ATestUser user1 = new ATestUser();
		user1.setPassword("password");
		user1.setRole(adminRole);

		try {
			usrManager.createUser(user1);
			fail("the username was empty; thus this item must not be created");
		} catch (FieldRequiredException e) {
			assertEquals("Wrong field info", "USERNAME", e.getFieldName());
			assertEquals("Wrong class info", "User", e.getClassName());
		} finally {
			FACTORY.releaseSession(brokenSession);
		}

	}

	// TODO: create tests with empty role
	// TODO: create tests with invalid role
}
