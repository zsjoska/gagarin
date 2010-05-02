package ro.gagarin;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestGroup;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.Group;

/**
 * Unit test for simple App.
 */
public class GroupTest {
    private String groupname = "Group_" + System.nanoTime();

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private Session session = null;

    @Before
    public void init() {
	this.session = TUtil.createTestSession();
    }

    @After
    public void close() {
	FACTORY.releaseSession(session);
    }

    @Test
    public void createGroup() throws Exception {

	UserDAO usrManager = FACTORY.getDAOManager().getUserDAO(session);

	ATestGroup group = new ATestGroup();
	group.setName(groupname);
	group.setDescription("test");
	group.setId(usrManager.createGroup(group));

	Group gr = usrManager.getGroupByName(groupname);

	assertEquals("Invalid ID", group.getId(), gr.getId());
	assertEquals("Invalid name", group.getName(), gr.getName());
	assertEquals("Invalid description", group.getDescription(), gr.getDescription());

    }

}
