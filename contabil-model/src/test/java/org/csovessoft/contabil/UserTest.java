package org.csovessoft.contabil;

import static org.junit.Assert.assertNull;

import org.csovessoft.contabil.user.User;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class UserTest {
	private String username = "_User_" + System.currentTimeMillis();
	private UserManager usrManager = ModelFactory.getUserManager();

	@Test
	public void getUserByNameInexistent() {
		User user = usrManager.getUserByUsername(username);
		assertNull("The user could not exists", user);
	}
}
