package ro.gagarin;

import java.util.List;

import org.junit.Test;

/**
 * Unit test for session.
 */
public class AuthServiceTest {

    @Test
    public void testSessionCreateLoginLogout() throws WSException_Exception {
	AuthenticationService service = new AuthenticationService();
	Authentication api = service.getAuthenticationPort();
	String session = api.createSession(null, "TEST");
	api.login(session, "admin", "password", null);
	List<WsEffectivePermission> cup = api.getCurrentUserPermissions(session);
	WsEffectivePermission wsEffectivePermission = cup.get(0);
	System.err.println(wsEffectivePermission.getName());
	System.err.println(wsEffectivePermission.getCategory());
	System.err.println(wsEffectivePermission.getId());
	System.err.println(wsEffectivePermission.getPermissions());
	api.logout(session);
    }

}
