package ro.gagarin;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.session.Session;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.Admin;
import ro.gagarin.ws.Authentication;

/**
 * Unit test for simple App.
 */
public class MethodAccessTest {

    private static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private static Authentication authentication = new Authentication();
    private static Admin adminService = new Admin();

    private String session = null;

    private Session aDummySession;

    @Before
    public void setUp() throws Exception {

	cleanup();

	aDummySession = TUtil.createTestSession();
	this.session = authentication.createSession(null, "TEST");
    }

    @After
    public void shutdown() throws Exception {
	authentication.logout(session);
	FACTORY.releaseSession(aDummySession);
	cleanup();
    }

    private void cleanup() throws OperationException, DataConstraintException, ItemNotFoundException {

	Session cleanupSession = TUtil.createTestSession();

	UserDAO userDAO = FACTORY.getDAOManager().getUserDAO(cleanupSession);
	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(cleanupSession);

	User user = userDAO.getUserByUsername("weakUser");
	if (user != null)
	    userDAO.deleteUser(user);
	UserRole role = roleDAO.getRoleByName("weak");
	if (role != null)
	    roleDAO.deleteRole(role);
	role = roleDAO.getRoleByName("stronger");
	if (role != null)
	    roleDAO.deleteRole(role);
	FACTORY.releaseSession(cleanupSession);

    }

    private UserPermission findPermission(PermissionEnum create_user, List<UserPermission> allPermissions) {
	for (UserPermission userPermission : allPermissions) {
	    if (create_user.name().equals(userPermission.getPermissionName()))
		return userPermission;
	}
	fail("permission " + create_user.name() + " was not found");
	return null;
    }
}
