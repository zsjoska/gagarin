package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.config.ConfigScope;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserRole;
import ro.gagarin.user.UserStatus;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.Admin;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.executor.WSException;
import ro.gagarin.ws.objects.WSConfig;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.objects.WSStatistic;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;

/**
 * Unit test for simple App.
 */
public class UserServiceTest {
    private static Authentication authentication = new Authentication();
    private static String username = "_User_" + System.currentTimeMillis();
    private static String session;
    private static Admin userService = new Admin();

    @BeforeClass
    public static void startup() throws WSException {
	session = authentication.createSession(null, "TEST");
	authentication.login(session, "admin", "password", null);
    }

    @Test
    public void testCreateUser() throws SessionNotFoundException, PermissionDeniedException, OperationException,
	    LoginRequiredException, WSException {

	Admin userService = new Admin();

	WSUser user = new WSUser();
	user.setUsername(username);
	user.setPassword("password");
	user.setStatus(UserStatus.ACTIVE);

	userService.createUser(session, user);
	// TODO:(4) check created user
    }

    @Test
    public void testCreateRole() throws WSException, SessionNotFoundException, OperationException,
	    PermissionDeniedException, LoginRequiredException {
	Admin userService = new Admin();

	// check that ID is enough
	List<WSUserPermission> allPermissionList = userService.getAllPermissionList(session);
	WSUserPermission permByID = new WSUserPermission();
	permByID.setId(allPermissionList.get(0).getId());

	// check that name is enough
	WSUserPermission[] perms = new WSUserPermission[] { new WSUserPermission(PermissionEnum.DELETE.name()),
		new WSUserPermission(PermissionEnum.CREATE.name()), permByID };

	UserRole role = userService.createRoleWithPermissions(session, "WONDER_ROLE", perms);
	List<WSUserPermission> rolePermissions = userService.getRolePermissions(session, new WSUserRole("WONDER_ROLE"));
	try {
	    for (WSUserPermission perm : perms) {
		WSUserPermission found = null;
		for (WSUserPermission p : rolePermissions) {
		    if (perm.getPermissionName() != null
			    && perm.getPermissionName().equalsIgnoreCase(p.getPermissionName())) {
			found = p;
		    }
		    if (perm.getId() != null && perm.getId().equals(p.getId())) {
			found = p;
		    }
		}
		if (found == null) {
		    fail(perm.getPermissionName() + " was not found");
		}

	    }
	} finally {
	    userService.deleteRole(session, new WSUserRole(role));
	}
    }

    @Test
    public void testListUsers() throws WSException {

	Admin userService = new Admin();

	List<WSUser> users = userService.getUsers(session);
	for (WSUser wsUser : users) {
	    System.err.println(wsUser.getUsername());
	}
    }

    @Test
    public void testConfigGet() throws Exception {

	List<WSConfig> configEntries = userService.getConfigEntries(session);
	userService.setConfigEntry(session, configEntries.get(0));

	TUtil.waitDBImportToHappen();

	configEntries = userService.getConfigEntries(session);
	assertEquals("We expect a config scope of DB", ConfigScope.DB, configEntries.get(0).getConfigScope());
    }

    @Test
    public void getLogEntries() throws Exception {
	// TODO:(4) add some more meaningful test
	List<WSLogEntry> logEntries = userService.getLogEntries(session, null);
	// for (WSLogEntry wsLogEntry : logEntries) {
	// System.out.println(wsLogEntry);
	// }
	assertNotNull(logEntries);
	assertTrue(logEntries.size() > 0);
    }

    @Test
    public void getSessionList() throws Exception {
	List<WSExportedSession> sessionList = userService.getSessionList(session);
	// for (WSExportedSession wsExportedSession : sessionList) {
	// System.out.println(wsExportedSession);
	// }
    }

    @Test
    public void getStatistics() throws Exception {
	Statistic testStat = Statistic.getByName("_test_statistic_");
	testStat.addDuration(10);
	testStat.addDuration(7);
	testStat.addDuration(20);
	testStat.addDuration(3);
	List<WSStatistic> list = userService.getStatistics(session, "_test_statistic_");
	for (WSStatistic stat : list) {
	    System.out.println(stat);
	}
	assertEquals(1, list.size());
	WSStatistic gotStat = list.get(0);
	assertEquals(4, gotStat.getCount());
	assertEquals(40, gotStat.getTotalDuration());
	assertEquals(20, gotStat.getMax());
	assertEquals(3, gotStat.getMin());
    }
}
