package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.application.objects.AppUser;
import ro.gagarin.config.Config;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestUser;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;
import ro.gagarin.user.UserStatus;

/**
 * Unit test for simple App.
 */
public class UserTest {
    private String username = "User_" + System.nanoTime();

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private Session session = null;
    private RoleDAO roleDAO;
    private UserDAO usrDAO;

    @Before
    public void init() throws Exception {
	this.session = TUtil.createTestSession();
	usrDAO = FACTORY.getDAOManager().getUserDAO(session);
	roleDAO = FACTORY.getDAOManager().getRoleDAO(session);

	username = "User_" + System.nanoTime();
    }

    @After
    public void close() {
	FACTORY.releaseSession(session);
    }

    private ConfigurationManager configManager = FACTORY.getConfigurationManager();

    @Test
    public void getUserByNameInexistent() throws OperationException {
	User user = usrDAO.getUserByUsername(username);
	assertNull("The user could not exists", user);
    }

    @Test
    public void createUser() throws ItemNotFoundException, DataConstraintException, OperationException {

	AppUser user = new AppUser();
	user.setName("Name Of User");
	user.setUsername(username);
	user.setPassword("password" + username);
	user.setEmail(username + "@gagarin.ro");
	user.setPhone("any kind of phone");
	user.setStatus(UserStatus.ACTIVE);
	user.setAuthentication("INTERNAL");
	user.setId(usrDAO.createUser(user));

	User user2 = usrDAO.getUserByUsername(username);

	assertNotNull("User was not found", user2);
	assertEquals("id does not match", user.getId(), user2.getId());
	assertEquals("name does not match", user.getName(), user2.getName());
	assertEquals("email does not match", user.getEmail(), user2.getEmail());
	assertEquals("phone does not match", user.getPhone(), user2.getPhone());
	assertEquals("username does not match", user.getUsername(), user2.getUsername());
	assertEquals("authentication should be filled", user2.getAuthentication(), "INTERNAL");
	assertEquals("status does not match", user.getStatus(), user2.getStatus());
	assertNotNull("created should be filled", user2.getCreated());

	usrDAO.deleteUser(user);
	assertNull("We just deleted the user; must not exists", usrDAO.getUserByUsername(username));
    }

    @Test
    public void createUserNegative() throws Exception {

	UserRole adminRole = roleDAO.getRoleByName(configManager.getString(Config.ADMIN_ROLE_NAME));
	assertNotNull("this test requires application setup", adminRole);

	try {
	    AppUser user = new AppUser();
	    user.setName("");
	    user.setUsername("");
	    user.setPassword("");
	    user.setEmail("");
	    user.setAuthentication("INTERNAL");
	    user.setPhone("");
	    long userid = usrDAO.createUser(user);
	    user.setId(userid);
	    usrDAO.deleteUser(user);
	    fail("The user shouldn't be created");
	} catch (DataConstraintException e) {
	    assertEquals("Invalid error code was thrown", ErrorCodes.FIELD_REQUIRED, e.getErrorCode());
	}
    }

    // @Test
    // public void usersWiththeSameID() throws ItemNotFoundException,
    // DataConstraintException,
    // OperationException {
    //
    // Session brokenSession = TUtil.createTestSession();
    //
    // UserDAO usrManager = FACTORY.getDAOManager().getUserDAO(brokenSession);
    // RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(brokenSession);
    //
    // UserRole adminRole = roleManager.getRoleByName(configManager
    // .getString(Config.ADMIN_ROLE_NAME));
    //
    // ATestUser user1 = new ATestUser();
    // user1.setUsername("UserName1");
    // user1.setPassword("password");
    // user1.setRole(adminRole);
    //
    // ATestUser user2 = new ATestUser();
    // user2.setUsername("UserName2");
    // user2.setPassword("password");
    // user2.setRole(adminRole);
    //
    // user2.setId(user1.getId());
    //
    // usrManager.createUser(user1);
    //
    // assertNotNull(usrManager.getUserByUsername("UserName1"));
    //
    // try {
    // usrManager.createUser(user2);
    // fail("the userid is the same; thus this item must not be created");
    // } catch (ItemExistsException e) {
    // assertEquals("Wrong field info", "ID", e.getFieldName());
    // assertEquals("Wrong class info", "User", e.getClassName());
    // } finally {
    // FACTORY.releaseSession(brokenSession);
    // }
    //
    // usrManager = FACTORY.getDAOManager().getUserDAO(session);
    // assertNull("Transaction rolback test",
    // usrManager.getUserByUsername("UserName1"));
    //
    // }

    @Test
    public void usersWiththeSameUsername() throws ItemNotFoundException, DataConstraintException, OperationException {

	ATestUser user1 = new ATestUser();
	user1.setUsername("UserName2");
	user1.setPassword("password");
	user1.setStatus(UserStatus.ACTIVE);

	ATestUser user2 = new ATestUser();
	user2.setUsername("UserName2");
	user2.setPassword("password");
	user2.setStatus(UserStatus.ACTIVE);

	usrDAO.createUser(user1);

	assertNotNull(usrDAO.getUserByUsername("UserName2"));

	try {
	    usrDAO.createUser(user2);
	    fail("the username is the same; thus this item must not be created");
	} catch (ItemExistsException e) {
	    assertEquals("Wrong field info", "USERNAME", e.getFieldName());
	} finally {
	    FACTORY.releaseSession(session);
	}

	session = TUtil.createTestSession();
	usrDAO = FACTORY.getDAOManager().getUserDAO(session);
	assertNull("Transaction rolback test", usrDAO.getUserByUsername("UserName2"));

    }

    @Test
    public void usersWithoutUsername() throws ItemNotFoundException, DataConstraintException, OperationException {

	Session brokenSession = TUtil.createTestSession();

	UserDAO usrDAO = FACTORY.getDAOManager().getUserDAO(brokenSession);

	ATestUser user1 = new ATestUser();
	user1.setPassword("password");
	user1.setAuthentication("INTERNAL");

	try {
	    usrDAO.createUser(user1);
	    fail("the username was empty; thus this item must not be created");
	} catch (FieldRequiredException e) {
	    assertEquals("Wrong field info", "username", e.getFieldName());
	} finally {
	    FACTORY.releaseSession(brokenSession);
	}

    }

    @Test
    public void updateUser() throws Exception {
	AppUser user = new AppUser();
	user.setName("Name Of User");
	user.setUsername(username + "_1");
	user.setPassword("password" + username);
	user.setEmail(username + "_1@gagarin.ro");
	user.setPhone("any kind of phone");
	user.setStatus(UserStatus.ACTIVE);
	long userId = usrDAO.createUser(user);

	AppUser user2 = new AppUser();
	user2.setId(userId);

	user2.setAuthentication("INTERNAL");
	user2.setEmail(username + "_2@gagarin.ro");
	user2.setName("another name");
	user2.setPassword("test");
	user2.setPhone("112233");
	user2.setStatus(UserStatus.SUSPENDED);
	user2.setUsername(username + "_2");

	usrDAO.updateUser(user2);

	User user3 = usrDAO.getUserByUsername(username + "_1");
	assertNull("the user had to be renamed", user3);

	user3 = usrDAO.getUserByUsername(username + "_2");
	assertNotNull("the user had to be renamed", user3);

	assertEquals("id does not match", userId, user3.getId());
	assertEquals("name does not match", user2.getName(), user3.getName());
	assertEquals("email does not match", user2.getEmail(), user3.getEmail());
	assertEquals("phone does not match", user2.getPhone(), user3.getPhone());
	assertEquals("username does not match", user2.getUsername(), user3.getUsername());
	assertEquals("authentication should be filled", user2.getAuthentication(), user3.getAuthentication());
	assertEquals("status does not match", user2.getStatus(), user3.getStatus());
    }
}
