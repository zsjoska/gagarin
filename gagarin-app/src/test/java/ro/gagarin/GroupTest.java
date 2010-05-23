package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestGroup;
import ro.gagarin.testobjects.ATestUser;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.user.UserStatus;

/**
 * Group functionality related tests
 */
public class GroupTest {
    private String groupname = "Group_" + System.nanoTime();

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();
    private UserDAO usrManager = null;

    private Session session = null;

    @Before
    public void init() throws OperationException {
	this.session = TUtil.createTestSession();
	this.usrManager = FACTORY.getDAOManager().getUserDAO(session);
    }

    @After
    public void close() {
	FACTORY.releaseSession(session);
    }

    @Test
    public void createDeleteGroup() throws Exception {

	UserDAO usrManager = FACTORY.getDAOManager().getUserDAO(session);

	ATestGroup group = new ATestGroup();
	group.setName(groupname);
	group.setDescription("test");
	group.setId(usrManager.createGroup(group));

	Group gr = usrManager.getGroupByName(groupname);
	assertNotNull("The group should exists", gr);
	assertEquals("Invalid ID", group.getId(), gr.getId());
	assertEquals("Invalid name", group.getName(), gr.getName());
	assertEquals("Invalid description", group.getDescription(), gr.getDescription());

	List<Group> groups = usrManager.getGroups();
	gr = groups.get(groups.size() - 1);

	assertEquals("Invalid ID", group.getId(), gr.getId());
	assertEquals("Invalid name", group.getName(), gr.getName());
	assertEquals("Invalid description", group.getDescription(), gr.getDescription());

	usrManager.deleteGroup(gr);
	gr = usrManager.getGroupByName(groupname);
	assertNull("The group should not exists", gr);

    }

    @Test
    public void updateGroup() throws Exception {

	String name = groupname + "1";
	ATestGroup group = new ATestGroup();
	group.setName(name);
	group.setDescription("test");
	group.setId(usrManager.createGroup(group));

	ATestGroup gr2 = new ATestGroup();
	gr2.setId(group.getId());
	gr2.setDescription("TEST");
	usrManager.updateGroup(gr2);

	Group groupByName = usrManager.getGroupByName(name);
	assertEquals("TEST", groupByName.getDescription());
	assertEquals(name, groupByName.getName());
	assertEquals(group.getId(), groupByName.getId());

	gr2 = new ATestGroup();
	gr2.setId(group.getId());
	gr2.setName(name + "update");
	usrManager.updateGroup(gr2);

	groupByName = usrManager.getGroupByName(name + "update");
	assertEquals("TEST", groupByName.getDescription());
	assertEquals(name + "update", groupByName.getName());
	assertEquals(group.getId(), groupByName.getId());

	gr2 = new ATestGroup();
	gr2.setId(group.getId());
	gr2.setDescription("TEST2");
	gr2.setName(name + "update2");
	usrManager.updateGroup(gr2);

	groupByName = usrManager.getGroupByName(name + "update2");
	assertEquals("TEST2", groupByName.getDescription());
	assertEquals(name + "update2", groupByName.getName());
	assertEquals(group.getId(), groupByName.getId());
    }

    @Test
    public void userGroupAssingnment() throws Exception {

	ATestUser user = new ATestUser();
	user.setUsername(groupname);
	user.setStatus(UserStatus.ACTIVE);
	user.setId(usrManager.createUser(user));

	ATestGroup group = new ATestGroup();
	group.setName(groupname);
	group.setId(usrManager.createGroup(group));

	usrManager.assignUserToGroup(user, group);

	List<User> users = usrManager.getGroupUsers(group);
	assertNotNull(users);
	assertEquals(1, users.size());
	User aUser = users.get(0);
	assertEquals(user.getId(), aUser.getId());
	assertEquals(user.getUsername(), aUser.getUsername());

	List<Group> userGroups = usrManager.getUserGroups(user);
	assertNotNull(userGroups);
	assertEquals(1, userGroups.size());
	Group aGroup = userGroups.get(0);
	assertEquals(aGroup.getId(), aGroup.getId());
	assertEquals(aGroup.getName(), aGroup.getName());

	usrManager.unassignUserFromGroup(user, aGroup);
	userGroups = usrManager.getUserGroups(user);
	assertEquals(0, userGroups.size());
    }

    @Test
    public void userGroupAssingnmentInexistentGroup() throws Exception {

	ATestUser user = new ATestUser();
	user.setUsername(groupname + "_1");
	user.setStatus(UserStatus.ACTIVE);
	user.setId(usrManager.createUser(user));

	ATestGroup group = new ATestGroup();
	group.setName(groupname + "_NOT");
	// don't set Id since then will not look up in GB for existence

	try {
	    usrManager.assignUserToGroup(user, group);
	    fail("Inexistent group shouldn't be assigned");
	} catch (ItemNotFoundException e) {
	    assertEquals("Invalid error code", ErrorCodes.ITEM_NOT_FOUND, e.getErrorCode());
	}

	// TODO: check that no assignment was made
    }

    @Test
    public void userGroupAssingnmentInexistentUser() throws Exception {

	ATestUser user = new ATestUser();
	user.setUsername(groupname + "_NOT");
	// don't set Id since then will not look up in GB for existence

	ATestGroup group = new ATestGroup();
	group.setName(groupname + "_2");
	group.setId(usrManager.createGroup(group));

	try {
	    usrManager.assignUserToGroup(user, group);
	    fail("Inexistent user shouldn't be assigned");
	} catch (ItemNotFoundException e) {
	    assertEquals("Invalid error code", ErrorCodes.ITEM_NOT_FOUND, e.getErrorCode());
	}

	// TODO: check that no assignment was made
    }
}
