package ro.gagarin;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
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
	UserService userService = new UserService();

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
	UserService userService = new UserService();

	WSGroup group = new WSGroup();
	group.setName(groupname);
	group.setDescription(groupname + username);
	Long groupId = userService.createGroup(session, group);
	assertNotNull(groupId);
	assertTrue(groupId > 0L);

	userService.deleteGroup(session, group);
    }
}
