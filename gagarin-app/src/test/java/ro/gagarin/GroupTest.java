package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestGroup;
import ro.gagarin.testobjects.ATestUser;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

/**
 * Unit test for simple App.
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

	UserRole adminRole = TUtil.getAdminRole();

	ATestUser user = new ATestUser();
	user.setUsername(groupname);
	user.setRole(adminRole);
	user.setId(usrManager.createUser(user));

	ATestGroup group = new ATestGroup();
	group.setName(groupname);
	usrManager.createGroup(group);

	usrManager.assignUserToGroup(user, group);

	List<User> users = usrManager.getGroupUsers(group);
	assertNotNull(users);
	assertEquals(1, users.size());
	User aUser = users.get(0);
	assertEquals(user.getId(), aUser.getId());
	assertEquals(user.getUsername(), aUser.getUsername());

    }
}
