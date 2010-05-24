package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import ro.gagarin.application.objects.AppUser;
import ro.gagarin.config.Config;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.manager.SessionManager;
import ro.gagarin.session.Session;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.UserRole;
import ro.gagarin.user.UserStatus;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.executor.WSException;

/**
 * Unit test for simple App.
 */
public class SessionTest {
    private static final transient Logger LOG = Logger.getLogger(SessionTest.class);

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private Authentication authentication = new Authentication();
    private String username = "_User_" + System.currentTimeMillis();

    private Session session = new Session();

    // TODO:(5) add test with null session for all WS methods

    // TODO:(5) add test without login for all WS methods

    @Test
    public void testSuccessLogin() throws OperationException, DataConstraintException, ItemNotFoundException,
	    WSException {

	session = TUtil.createTestSession();

	UserDAO userManager = FACTORY.getDAOManager().getUserDAO(session);
	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

	List<UserRole> allRoles = roleManager.getAllRoles();
	LOG.debug("Roles in system:");
	for (UserRole userRole : allRoles) {
	    LOG.debug(userRole.getRoleName());
	}
	LOG.debug("End roles listing");

	AppUser user = new AppUser();
	user.setUsername("1" + username);
	user.setPassword("password1");
	user.setStatus(UserStatus.ACTIVE);
	userManager.createUser(user);
	FACTORY.releaseSession(session);

	String session = authentication.createSession(null, "TEST");
	assertNotNull(session);

	authentication.login(session, "1" + username, "password1", null);

	authentication.logout(session);
    }

    @Test
    public void testFailedLogin() throws SessionNotFoundException, ItemNotFoundException, DataConstraintException,
	    OperationException, WSException {

	session = TUtil.createTestSession();

	UserDAO userManager = FACTORY.getDAOManager().getUserDAO(session);

	DBUser user = new DBUser();
	user.setUsername("2" + username);
	user.setPassword("password2");
	user.setStatus(UserStatus.ACTIVE);
	userManager.createUser(user);

	FACTORY.releaseSession(session);

	String session = authentication.createSession(null, "TEST");
	assertNotNull(session);

	try {
	    authentication.login(session, "user2_", "password2", null);
	    fail("The user does not exists");
	} catch (WSException e) {
	    assertEquals(ErrorCodes.ITEM_NOT_FOUND, e.getErrorCode());
	    // the exception was expected
	}
	try {
	    authentication.login(session, "user2", "password2_", null);
	    fail("The user and password does not match; thus authentication must fail");
	} catch (WSException e) {
	    assertEquals(ErrorCodes.ITEM_NOT_FOUND, e.getErrorCode());
	    // the exception was expected
	}
    }

    @Test
    public void testSessionDeletion() throws OperationException, DataConstraintException, ItemNotFoundException,
	    WSException {
	session = TUtil.createTestSession();

	UserDAO userManager = FACTORY.getDAOManager().getUserDAO(session);

	DBUser user = new DBUser();
	user.setUsername("3" + username);
	user.setPassword("password3");
	user.setStatus(UserStatus.ACTIVE);
	userManager.createUser(user);

	FACTORY.releaseSession(session);

	String session = authentication.createSession(null, "TEST");
	assertNotNull(session);

	authentication.logout(session);
	try {
	    authentication.login(session, "3" + username, "password3", null);
	    fail("The login must fail since the session was deleted");
	} catch (WSException e) {
	    assertEquals("Invalid status returned", ErrorCodes.SESSION_NOT_FOUND, e.getErrorCode());
	    assertEquals("Not enough information in detail", session, e.getDetail());
	}

    }

    @Test
    public void testSessionExpiration() throws InterruptedException, SessionNotFoundException, OperationException {

	TUtil.setDBImportRate(100);

	session = FACTORY.getSessionManager().createSession(null, null, FACTORY);
	FACTORY.getSessionManager().acquireSession(session.getSessionString());
	ConfigurationManager cfgManager = FACTORY.getConfigurationManager();

	String oldSessionTimeout = cfgManager.getString(Config.USER_SESSION_TIMEOUT);

	try {
	    cfgManager.setConfigValue(session, Config.USER_SESSION_TIMEOUT, "100");
	    FACTORY.releaseSession(session);

	    TUtil.waitDBImportToHappen();

	    SessionManager sessionManager = FACTORY.getSessionManager();
	    Session testSession = FACTORY.getSessionManager().createSession(null, null, FACTORY);
	    assertNotNull(testSession);
	    assertEquals("We just set the timeout to 100", 100, testSession.getSessionTimeout());

	    try {
		testSession = sessionManager.acquireSession(testSession.getSessionString());
	    } catch (SessionNotFoundException e) {
		fail("The session should be active");
	    }
	    assertNotNull(testSession);

	    Thread.sleep(110);
	    try {
		testSession = sessionManager.acquireSession(testSession.getSessionString());
		fail("The session must be expired at this time");
	    } catch (SessionNotFoundException e) {
		// expected exception
	    }
	} finally {
	    FACTORY.getSessionManager().acquireSession(session.getSessionString());
	    cfgManager.setConfigValue(session, Config.USER_SESSION_TIMEOUT, oldSessionTimeout);
	    FACTORY.releaseSession(session);
	    TUtil.waitDBImportToHappen();
	}

	TUtil.resetDBImportRate();
    }
}
