package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.UserService;
import ro.gagarin.ws.WSException;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserRole;

/**
 * Unit test for simple App.
 */
public class MethodAccessTest {

    private static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private static Authentication authentication = new Authentication();
    private static UserService userService = new UserService();

    private String session = null;

    private Session aDummySession;

    @Before
    public void setUp() throws WSException, OperationException, DataConstraintException {

	cleanup();

	aDummySession = TUtil.createTestSession();
	this.session = authentication.createSession(null, null);
    }

    @After
    public void shutdown() throws OperationException, DataConstraintException {
	authentication.logout(session);
	FACTORY.releaseSession(aDummySession);
	cleanup();
    }

    private void cleanup() throws OperationException, DataConstraintException {

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

    @Test
    public void createUserAccess() throws OperationException, DataConstraintException, ItemNotFoundException,
	    WSException, SessionNotFoundException, LoginRequiredException {

	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(aDummySession);
	UserDAO userDAO = FACTORY.getDAOManager().getUserDAO(aDummySession);

	// long id =
	// createUserRoleWithPermissions(aDummySession,"weakUser","weak" );

	List<UserPermission> allPermissions = roleDAO.getAllPermissions();
	assertTrue(allPermissions.size() > 3);
	WSUserRole role1 = new WSUserRole();
	role1.setRoleName("weak");
	role1.setId(roleDAO.createRole(role1));
	WSUser weakUser = new WSUser();
	weakUser.setUsername("weakUser");
	weakUser.setPassword("password");
	weakUser.setRole(role1);
	weakUser.setId(userDAO.createUser(weakUser));

	// have it committed so other sessions to have access to it
	FACTORY.releaseSession(aDummySession);
	// and recreate objects

	aDummySession = TUtil.createTestSession();
	roleDAO = FACTORY.getDAOManager().getRoleDAO(aDummySession);
	userDAO = FACTORY.getDAOManager().getUserDAO(aDummySession);

	authentication.login(session, "weakUser", "password", null);
	WSUser notCreated = new WSUser();
	notCreated.setUsername("any");
	notCreated.setPassword("any");
	notCreated.setRole(role1);
	try {
	    userService.createUser(session, notCreated);
	    fail("weakUser's role not enugh to create a user");
	} catch (PermissionDeniedException e) {
	    // expected
	}

	roleDAO.assignPermissionToRole(role1, findPermission(PermissionEnum.CREATE_USER, allPermissions));
	WSUserRole role2 = new WSUserRole();
	role2.setRoleName("stronger");
	role2.setId(roleDAO.createRole(role2));
	roleDAO.assignPermissionToRole(role2, findPermission(PermissionEnum.LIST_ROLES, allPermissions));

	List<UserPermission> left = roleDAO.substractUsersRolePermissions(role2, role1);
	assertEquals(1, left.size());

	// have it committed so other sessions to have access to it
	FACTORY.releaseSession(aDummySession);
	// and recreate objects

	aDummySession = TUtil.createTestSession();

	roleDAO = FACTORY.getDAOManager().getRoleDAO(aDummySession);
	userDAO = FACTORY.getDAOManager().getUserDAO(aDummySession);

	notCreated.setRole(role2);
	try {
	    userService.createUser(session, notCreated);
	    fail("weakUser could not create a user with LIST_ROLES");
	} catch (PermissionDeniedException e) {
	    // expected
	}

	userDAO.deleteUser(weakUser);
	roleDAO.deleteRole(role1);
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
