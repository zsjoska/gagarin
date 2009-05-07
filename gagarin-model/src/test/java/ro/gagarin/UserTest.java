package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.config.Config;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.hibernate.objects.DBUser;
import ro.gagarin.session.Session;
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
	public void createUser() throws FieldRequiredException, UserAlreadyExistsException {

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
}
