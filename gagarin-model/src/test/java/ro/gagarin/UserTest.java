package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.config.Config;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.hibernate.objects.DBUser;
import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestUser;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

/**
 * Unit test for simple App.
 */
public class UserTest {
	private String username = "User_" + System.nanoTime();

	private Session session = null;

	@Before
	public void init() {
		this.session = ModelFactory.getSessionManager().createSession(null, null);
	}

	@After
	public void close() {
		ModelFactory.releaseSession(session);
	}

	private ConfigurationManager configManager = ModelFactory.getConfigurationManager(session);

	@Test
	public void getUserByNameInexistent() {
		UserDAO usrManager = ModelFactory.getDAOManager().getUserDAO(session);
		User user = usrManager.getUserByUsername(username);
		assertNull("The user could not exists", user);
	}

	@Test
	public void createUser() throws FieldRequiredException, UserAlreadyExistsException,
			ItemNotFoundException {

		UserDAO usrManager = ModelFactory.getDAOManager().getUserDAO(session);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);

		UserRole adminRole = roleManager.getRoleByName(configManager
				.getString(Config.ADMIN_ROLE_NAME));
		assertNotNull("this test requires application setup", adminRole);

		DBUser user = new DBUser();
		user.setName("Name Of User");
		user.setUsername(username);
		user.setPassword("password" + username);
		user.setRole(adminRole);
		long userid = usrManager.createUser(user);
		User user2 = usrManager.getUserByUsername(username);

		assertEquals("id does not match", user.getId(), user2.getId());
		assertEquals("name does not match", user.getName(), user2.getName());
		assertEquals("username does not match", user.getUsername(), user2.getUsername());
		assertEquals("password does not match", user.getPassword(), user2.getPassword());
		usrManager.deleteUserById(userid);
		assertNull("We just deleted the user; must not exists", usrManager
				.getUserByUsername(username));
	}

	@Test
	public void usersWiththeSameID() throws FieldRequiredException, UserAlreadyExistsException,
			ItemNotFoundException {

		Session brokenSession = ModelFactory.getSessionManager().createSession(null, null);

		UserDAO usrManager = ModelFactory.getDAOManager().getUserDAO(brokenSession);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(brokenSession);

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
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		ModelFactory.releaseSession(brokenSession);

		usrManager = ModelFactory.getDAOManager().getUserDAO(session);
		assertNull(usrManager.getUserByUsername("UserName1"));

	}

	@Test
	public void usersWiththeSameUsername() throws FieldRequiredException,
			UserAlreadyExistsException, ItemNotFoundException {

		Session brokenSession = ModelFactory.getSessionManager().createSession(null, null);

		UserDAO usrManager = ModelFactory.getDAOManager().getUserDAO(brokenSession);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(brokenSession);

		UserRole adminRole = roleManager.getRoleByName(configManager
				.getString(Config.ADMIN_ROLE_NAME));

		ATestUser user1 = new ATestUser();
		user1.setUsername("UserName1");
		user1.setPassword("password");
		user1.setRole(adminRole);

		ATestUser user2 = new ATestUser();
		user2.setUsername("UserName1");
		user2.setPassword("password");
		user2.setRole(adminRole);

		usrManager.createUser(user1);

		assertNotNull(usrManager.getUserByUsername("UserName1"));

		try {
			usrManager.createUser(user2);
			fail("the username is the same; thus this item must not be created");
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		ModelFactory.releaseSession(brokenSession);

		usrManager = ModelFactory.getDAOManager().getUserDAO(session);
		assertNull(usrManager.getUserByUsername("UserName1"));

	}

	@Test
	public void usersWithoutUsername() throws UserAlreadyExistsException, ItemNotFoundException {

		UserDAO usrManager = ModelFactory.getDAOManager().getUserDAO(session);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);

		UserRole adminRole = roleManager.getRoleByName(configManager
				.getString(Config.ADMIN_ROLE_NAME));

		ATestUser user1 = new ATestUser();
		user1.setPassword("password");
		user1.setRole(adminRole);

		try {
			usrManager.createUser(user1);
			fail("the username was empty; thus this item must not be created");
		} catch (FieldRequiredException e) {
			System.out.println(e.getMessage());
		}

	}
}
