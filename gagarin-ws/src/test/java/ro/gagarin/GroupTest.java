package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.UserService;
import ro.gagarin.ws.executor.WSException;
import ro.gagarin.ws.objects.WSGroup;

public class GroupTest {
    private static Authentication authentication = new Authentication();
    private static String username = "_User_" + System.currentTimeMillis();
    private static String groupname = "_Group_" + System.currentTimeMillis();
    private static String session;
    private static UserService userService = new UserService();

    @BeforeClass
    public static void startup() throws WSException {
	session = authentication.createSession(null, "TEST");
	authentication.login(session, "admin", "password", null);
    }

    @Test
    public void testCreateGroup() throws WSException, SessionNotFoundException, OperationException,
	    PermissionDeniedException, LoginRequiredException {

	WSGroup group = new WSGroup();
	group.setName(groupname);
	group.setDescription(groupname + username);
	Long groupId = userService.createGroup(session, group);
	assertNotNull(groupId);
	assertTrue(groupId > 0L);

	group.setId(groupId);
	userService.deleteGroup(session, group);
    }

    @Test
    public void testDeleteGroupByName() throws WSException, SessionNotFoundException, OperationException,
	    PermissionDeniedException, LoginRequiredException {

	WSGroup group = new WSGroup();
	group.setName(groupname);
	group.setDescription(groupname + username);
	Long groupId = userService.createGroup(session, group);
	assertNotNull(groupId);
	assertTrue(groupId > 0L);

	userService.deleteGroup(session, group);
    }

    @Test
    public void updateGroup() throws Exception {

	String name = groupname + "1";
	WSGroup group = new WSGroup();
	group.setName(name);
	group.setDescription("test");
	group.setId(userService.createGroup(session, group));

	WSGroup gr2 = new WSGroup();
	gr2.setId(group.getId());
	gr2.setDescription("TEST");
	userService.updateGroup(session, gr2);

	// no getGroupByName in WS and no need for it, so going to lower level
	Session sameSession = BasicManagerFactory.getInstance().getSessionManager().acquireSession(session);
	UserDAO usrManager = BasicManagerFactory.getInstance().getDAOManager().getUserDAO(sameSession);
	Group groupByName = usrManager.getGroupByName(name);
	BasicManagerFactory.getInstance().releaseSession(sameSession);

	assertEquals("TEST", groupByName.getDescription());
	assertEquals(name, groupByName.getName());
	assertEquals(group.getId(), groupByName.getId());

	gr2 = new WSGroup();
	gr2.setId(group.getId());
	gr2.setName(name + "update");
	userService.updateGroup(session, gr2);

	sameSession = BasicManagerFactory.getInstance().getSessionManager().acquireSession(session);
	usrManager = BasicManagerFactory.getInstance().getDAOManager().getUserDAO(sameSession);
	groupByName = usrManager.getGroupByName(name + "update");
	BasicManagerFactory.getInstance().releaseSession(sameSession);

	assertEquals("TEST", groupByName.getDescription());
	assertEquals(name + "update", groupByName.getName());
	assertEquals(group.getId(), groupByName.getId());

	gr2 = new WSGroup();
	gr2.setId(group.getId());
	gr2.setDescription("TEST2");
	gr2.setName(name + "update2");
	userService.updateGroup(session, gr2);

	sameSession = BasicManagerFactory.getInstance().getSessionManager().acquireSession(session);
	usrManager = BasicManagerFactory.getInstance().getDAOManager().getUserDAO(sameSession);
	groupByName = usrManager.getGroupByName(name + "update2");
	BasicManagerFactory.getInstance().releaseSession(sameSession);

	assertEquals("TEST2", groupByName.getDescription());
	assertEquals(name + "update2", groupByName.getName());
	assertEquals(group.getId(), groupByName.getId());
    }

}
