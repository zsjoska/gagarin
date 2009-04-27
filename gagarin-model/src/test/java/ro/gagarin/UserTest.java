package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.user.User;

/**
 * Unit test for simple App.
 */
public class UserTest {
	private String username = "User_" + System.nanoTime();
	private UserManager usrManager = ModelFactory.getUserManager();

	@Test
	public void getUserByNameInexistent() {
		User user = usrManager.getUserByUsername(username);
		assertNull("The user could not exists", user);
	}

	@Test
	public void createUser() throws FieldRequiredException, UserAlreadyExistsException {
		User user = new User();
		user.setName("Name Of User");
		user.setUsername(username);
		user.setPassword("password" + username);
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
