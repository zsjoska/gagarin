package ro.gagarin;

import org.junit.Before;
import org.junit.Test;

import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.objects.WSUser;

public class RegistrationTest {

    private String username;

    @Before
    public void startup() {
	this.username = "User_" + System.nanoTime();
    }

    @Test
    public void testRegisterUser() throws Exception {
	Authentication authentication = new Authentication();
	String sessionId = authentication.createSession(null, "TEST");

	WSUser user = new WSUser();
	user.setEmail(username + "@email.com");
	user.setUsername(username);
	user.setName(username + " " + username);
	user.setPassword(username);

	String regKey = authentication.registerUser(sessionId, user);
    }
}
