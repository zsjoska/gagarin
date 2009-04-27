package org.csovessoft.contabil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.csovessoft.contabil.config.Config;
import org.csovessoft.contabil.exceptions.FieldRequiredException;
import org.csovessoft.contabil.exceptions.UserAlreadyExistsException;
import org.csovessoft.contabil.user.User;
import org.csovessoft.contabil.user.UserRole;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class UserTest {
	private String username = "User_" + System.nanoTime();
	private UserManager usrManager = ModelFactory.getUserManager();
	private RoleManager roleManager = ModelFactory.getRoleManager();
	private ConfigurationManager configManager = ModelFactory.getConfigurationManager();

	@Test
	public void getUserByNameInexistent() {
		User user = usrManager.getUserByUsername(username);
		assertNull("The user could not exists", user);
	}

	@Test
	public void createUser() throws FieldRequiredException, UserAlreadyExistsException {

		UserRole adminRole = roleManager.getRoleByName(configManager
				.getString(Config.ADMIN_ROLE_NAME));
		User user = new User();
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
