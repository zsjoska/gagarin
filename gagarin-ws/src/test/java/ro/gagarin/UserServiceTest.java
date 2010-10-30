package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.config.ConfigScope;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;
import ro.gagarin.user.UserStatus;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.Admin;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.executor.WSException;
import ro.gagarin.ws.objects.WSConfig;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.objects.WSOwner;
import ro.gagarin.ws.objects.WSProperty;
import ro.gagarin.ws.objects.WSPropertySet;
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
    private static Admin adminService = new Admin();

    @BeforeClass
    public static void startup() throws WSException {
	session = authentication.createSession(null, "TEST");
	authentication.login(session, "admin", "password", null);
    }

    private String uniqueId;

    @Before
    public void init() {
	this.uniqueId = TUtil.generateID("_test");
    }

    @Test
    public void testCreateUser() throws SessionNotFoundException, PermissionDeniedException, OperationException,
	    LoginRequiredException, WSException {

	Admin adminService = new Admin();

	WSUser user = new WSUser();
	user.setUsername(username);
	user.setPassword("password");
	user.setStatus(UserStatus.ACTIVE);

	adminService.createUser(session, user);
	// TODO:(4) check created user
    }

    @Test
    public void testCreateRole() throws WSException, SessionNotFoundException, OperationException,
	    PermissionDeniedException, LoginRequiredException {
	Admin adminService = new Admin();

	// check that ID is enough
	List<WSUserPermission> allPermissionList = adminService.getAllPermissionList(session);
	WSUserPermission permByID = new WSUserPermission();
	permByID.setId(allPermissionList.get(0).getId());

	// check that name is enough
	WSUserPermission[] perms = new WSUserPermission[] { new WSUserPermission(PermissionEnum.DELETE.name()),
		new WSUserPermission(PermissionEnum.CREATE.name()), permByID };

	UserRole role = adminService.createRoleWithPermissions(session, "WONDER_ROLE", perms);
	List<WSUserPermission> rolePermissions = adminService
		.getRolePermissions(session, new WSUserRole("WONDER_ROLE"));
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
	    adminService.deleteRole(session, new WSUserRole(role));
	}
    }

    @Test
    public void testListUsers() throws WSException {

	Admin adminService = new Admin();

	List<WSUser> users = adminService.getUsers(session);
	for (WSUser wsUser : users) {
	    System.err.println(wsUser.getUsername());
	}
    }

    @Test
    public void testConfigGet() throws Exception {

	List<WSConfig> configEntries = adminService.getConfigEntries(session);
	adminService.setConfigEntry(session, configEntries.get(0));

	TUtil.waitDBImportToHappen();

	configEntries = adminService.getConfigEntries(session);
	assertEquals("We expect a config scope of DB", ConfigScope.DB, configEntries.get(0).getConfigScope());
    }

    @Test
    public void getLogEntries() throws Exception {
	// TODO:(4) add some more meaningful test
	List<WSLogEntry> logEntries = adminService.getLogEntries(session, null);
	// for (WSLogEntry wsLogEntry : logEntries) {
	// System.out.println(wsLogEntry);
	// }
	assertNotNull(logEntries);
	assertTrue(logEntries.size() > 0);
    }

    @Test
    public void getSessionList() throws Exception {
	List<WSExportedSession> sessionList = adminService.getSessionList(session);
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
	List<WSStatistic> list = adminService.getStatistics(session, "_test_statistic_");
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

    @Test
    public void testGetControlEntityCategories() throws Exception {
	List<ControlEntityCategory> controlEntityCategories = adminService.getControlEntityCategories(session);
	assertEquals(ControlEntityCategory.values().length, controlEntityCategories.size());
	for (int i = 0; i < ControlEntityCategory.values().length; i++) {
	    assertEquals(ControlEntityCategory.values()[i], controlEntityCategories.get(i));
	}
    }

    @Test
    public void testGetControlEntityListForCategoryAdmin() throws Exception {
	List<WSControlEntity> controlEntityListForCategory = adminService.getControlEntityListForCategory(session,
		"ADMIN");
	assertEquals("We expect only one ADMIN entity", 1, controlEntityListForCategory.size());
	WSControlEntity wsControlEntity = controlEntityListForCategory.get(0);
	assertEquals(CommonControlEntities.ADMIN_CE.getName(), wsControlEntity.getName());
	assertEquals(CommonControlEntities.ADMIN_CE.getId(), wsControlEntity.getId());
	assertEquals(CommonControlEntities.ADMIN_CE.getCategory(), wsControlEntity.getCategory());
    }

    @Test
    public void testGetControlEntityListForCategoryGroups() throws Exception {
	List<WSControlEntity> controlEntityListForCategory = adminService.getControlEntityListForCategory(session,
		"GROUP");
	List<WSGroup> groups = adminService.getGroups(session);
	assertEquals(groups.size(), controlEntityListForCategory.size());
	HashMap<Long, Group> groupSet = new HashMap<Long, Group>();
	for (Group group : groups) {
	    groupSet.put(group.getId(), group);
	}

	for (WSControlEntity wsControlEntity : controlEntityListForCategory) {
	    Group group = groupSet.get(wsControlEntity.getId());
	    assertEquals(group.getName(), wsControlEntity.getName());
	}
    }

    @Test
    public void testgetOwners() throws Exception {
	List<WSOwner> owners = adminService.getOwners(session);
	List<WSUser> users = adminService.getUsers(session);
	List<WSGroup> groups = adminService.getGroups(session);

	assertEquals(users.size() + groups.size(), owners.size());
	HashMap<Long, Owner> ownersMap = new HashMap<Long, Owner>();
	for (User user : users) {
	    ownersMap.put(user.getId(), user);
	}
	for (Group group : groups) {
	    ownersMap.put(group.getId(), group);
	}
	for (WSOwner wsOwner : owners) {
	    Owner owner = ownersMap.get(wsOwner.getId());
	    assertEquals(owner.getType(), wsOwner.getType());
	    assertEquals(owner.getTitle(), wsOwner.getTitle());
	}
    }

    @Test
    public void testUpdateRole() throws Exception {
	WSUserPermission[] perms = new WSUserPermission[] { new WSUserPermission(PermissionEnum.CREATE),
		new WSUserPermission(PermissionEnum.LIST), new WSUserPermission(PermissionEnum.SELECT) };
	WSUserRole role = adminService.createRoleWithPermissions(session, uniqueId, perms);

	perms = new WSUserPermission[] { new WSUserPermission(PermissionEnum.CREATE),
		new WSUserPermission(PermissionEnum.UPDATE), new WSUserPermission(PermissionEnum.DELETE) };

	role.setRoleName(uniqueId + "_");
	adminService.updateRole(session, role, perms);

	List<WSUserPermission> rolePermissions = adminService.getRolePermissions(session, role);
	assertEquals(3, rolePermissions.size());
	assertEquals(PermissionEnum.UPDATE.name(), rolePermissions.get(0).getPermissionName());
	assertEquals(PermissionEnum.CREATE.name(), rolePermissions.get(1).getPermissionName());
	assertEquals(PermissionEnum.DELETE.name(), rolePermissions.get(2).getPermissionName());
    }

    @Test
    public void testUpdateRoleId() throws Exception {
	List<WSUserPermission> allPerms = adminService.getAllPermissionList(session);

	WSUserPermission perm1 = new WSUserPermission();
	perm1.setId(allPerms.get(1).getId());
	WSUserPermission perm2 = new WSUserPermission();
	perm2.setId(allPerms.get(2).getId());

	WSUserPermission[] perms = new WSUserPermission[] { perm1, perm2 };

	WSUserRole role = adminService.createRoleWithPermissions(session, uniqueId, perms);

	WSUserPermission perm3 = new WSUserPermission();
	perm3.setId(allPerms.get(3).getId());

	perms = new WSUserPermission[] { perm2, perm3 };

	role.setRoleName(uniqueId + "_");
	adminService.updateRole(session, role, perms);

	List<WSUserPermission> rolePermissions = adminService.getRolePermissions(session, role);
	assertEquals(2, rolePermissions.size());
	assertEquals(PermissionEnum.LIST.name(), rolePermissions.get(0).getPermissionName());
	assertEquals(PermissionEnum.UPDATE.name(), rolePermissions.get(1).getPermissionName());
    }

    @Test
    public void testCreateUpdateUserExtra() throws Exception {

	WSPropertySet props = new WSPropertySet();
	props.setId(WSPropertySet.getNextId());
	props.addField(new WSProperty().setFieldName("name").setFieldValue("me"));
	props.addField(new WSProperty().setFieldName("CNP").setFieldValue("123456789"));
	adminService.setUserExtra(session, props);

	WSPropertySet userExtra = adminService.getUserExtra(session, new WSUser().setId(props.getId()));

	System.out.println(userExtra);

	assertEquals(props.getId(), userExtra.getId());
	assertNotNull(userExtra.getTimestamp());
    }
}
