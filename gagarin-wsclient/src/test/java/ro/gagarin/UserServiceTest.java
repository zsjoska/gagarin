package ro.gagarin;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class UserServiceTest {

    private static String session = null;
    private static Authentication api = null;

    @BeforeClass
    public static void setUp() throws WSException_Exception {
	AuthenticationService service = new AuthenticationService();
	api = service.getAuthenticationPort();
	session = api.createSession(null, "TEST");
	api.login(session, "admin", "password", null);
    }

    @AfterClass
    public static void shutDown() throws WSException_Exception {
	api.logout(session);
    }

    @Test
    public void createUser() throws WSException_Exception {
	AdminService service = new AdminService();
	Admin userAPI = service.getAdminPort();

	WsUser user = new WsUser();
	user.setUsername("wsUser1" + System.nanoTime());
	user.setPassword("wspassword1");
	user.setStatus(UserStatus.ACTIVE);
	userAPI.createUser(session, user);
    }
}
