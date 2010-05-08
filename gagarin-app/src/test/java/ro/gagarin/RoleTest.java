package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestUser;
import ro.gagarin.testobjects.ATestUserPermission;
import ro.gagarin.testobjects.ATestUserRole;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

/**
 * Unit test for simple App.
 */
public class RoleTest {

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private Session session = null;

    @Before
    public void init() throws SessionNotFoundException {
	this.session = TUtil.createTestSession();
    }

    @After
    public void close() {
	FACTORY.releaseSession(session);
    }

    @Test
    public void createGetDeleteSimpleRole() throws Exception {
	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
	ATestUserRole role = new ATestUserRole();
	role.setRoleName("A_ROLE");
	role.setId(roleManager.createRole(role));

	UserRole role2 = roleManager.getRoleByName("A_ROLE");
	assertNotNull(role2);
	assertEquals(role.getId(), role2.getId());
	assertEquals(role.getRoleName(), role2.getRoleName());

	roleManager.deleteRole(role2);
	role2 = roleManager.getRoleByName("A_ROLE");
	assertNull(role2);
    }

    @Test
    public void createGetDeleteSimplePermission() throws Exception {
	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);

	ATestUserPermission perm = new ATestUserPermission();
	perm.setPermissionName("A_PERMISSION");
	perm.setId(roleManager.createPermission(perm));

	UserPermission perm2 = roleManager.getPermissionByName("A_PERMISSION");
	assertNotNull(perm2);
	assertEquals(perm.getId(), perm2.getId());
	assertEquals(perm.getPermissionName(), perm2.getPermissionName());

	roleManager.deletePermission(perm);
	perm2 = roleManager.getPermissionByName("A_PERMISSION");
	assertNull(perm2);
    }

    @Test
    public void createRoleWithPermission() throws ItemNotFoundException, DataConstraintException, OperationException {
	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
	ATestUserRole role = new ATestUserRole();
	role.setRoleName("C_ROLE");

	ATestUserPermission perm = new ATestUserPermission();
	perm.setPermissionName("C_PERMISSION");

	perm.setId(roleManager.createPermission(perm));
	role.setId(roleManager.createRole(role));
	roleManager.assignPermissionToRole(role, perm);

	UserRole role2 = roleManager.getRoleByName("C_ROLE");
	assertNotNull(role2);
	Set<UserPermission> perms = roleManager.getRolePermissions(role2);
	assertNotNull(perms);
	assertEquals(1, perms.size());

	UserPermission perm2 = roleManager.getPermissionByName("C_PERMISSION");
	assertNotNull(perm2);
	Set<UserRole> roles = roleManager.getPermissionRoles(perm2);
	assertNotNull(roles);
	assertEquals(1, roles.size());

	roleManager.deleteRole(role);
	roleManager.deletePermission(perm);

    }

    @Test
    public void createRoleWith2Permissions() throws ItemNotFoundException, DataConstraintException, OperationException {
	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
	ATestUserRole role = new ATestUserRole();
	role.setRoleName("C_ROLE");

	ATestUserPermission perm1 = new ATestUserPermission();
	perm1.setPermissionName("C_PERMISSION1");
	ATestUserPermission perm2 = new ATestUserPermission();
	perm2.setPermissionName("C_PERMISSION2");

	perm1.setId(roleManager.createPermission(perm1));
	perm2.setId(roleManager.createPermission(perm2));
	role.setId(roleManager.createRole(role));

	roleManager.assignPermissionToRole(role, perm1);
	roleManager.assignPermissionToRole(role, perm2);

	UserRole role2 = roleManager.getRoleByName("C_ROLE");
	assertNotNull(role2);

	Set<UserPermission> permissions = roleManager.getRolePermissions(role2);
	assertNotNull(permissions);
	assertEquals(2, permissions.size());

	UserPermission perm_1 = roleManager.getPermissionByName("C_PERMISSION1");
	assertNotNull(perm_1);

	Set<UserRole> roles_1 = roleManager.getPermissionRoles(perm_1);
	assertNotNull(roles_1);
	assertEquals(1, roles_1.size());

	UserPermission perm_2 = roleManager.getPermissionByName("C_PERMISSION2");
	assertNotNull(perm_2);
	Set<UserRole> roles_2 = roleManager.getPermissionRoles(perm_2);
	assertNotNull(roles_2);
	assertEquals(1, roles_2.size());

	roleManager.deleteRole(role);
	roleManager.deletePermission(perm1);
	roleManager.deletePermission(perm2);

    }

    @Test
    public void addPermissionToRole() throws ItemNotFoundException, DataConstraintException, OperationException {
	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
	UserRole role = roleManager.getRoleByName("B_ROLE");
	if (role == null) {
	    ATestUserRole role2 = new ATestUserRole();
	    role2.setRoleName("B_ROLE");
	    role2.setId(roleManager.createRole(role2));
	    role = role2;
	}

	Set<UserPermission> rolePermissions = roleManager.getRolePermissions(role);

	ATestUserPermission permission = new ATestUserPermission();
	permission.setPermissionName("PERM" + rolePermissions.size());

	permission.setId(roleManager.createPermission(permission));
	roleManager.assignPermissionToRole(role, permission);
    }

    @Test
    public void testGetUsersWithRole() throws DataConstraintException, OperationException, ItemNotFoundException {
	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
	UserDAO userDAO = FACTORY.getDAOManager().getUserDAO(session);

	ATestUserRole role = new ATestUserRole();
	role.setRoleName("A_ROLE");
	role.setId(roleManager.createRole(role));

	ATestUser user = new ATestUser();
	user.setName("name");
	user.setUsername("username");
	user.setPassword("password");
	user.setRole(role);
	user.setId(userDAO.createUser(user));

	List<User> usersWithRole = userDAO.getUsersWithRole(role);

	assertNotNull(usersWithRole);
	assertEquals(1, usersWithRole.size());
	assertEquals(user.getId(), usersWithRole.get(0).getId());

	userDAO.deleteUser(user);
	roleManager.deleteRole(role);
    }

    @Test
    public void createRoleWithSameName() throws DataConstraintException, OperationException, SessionNotFoundException {

	Session brokenSession = TUtil.createTestSession();

	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserRole role = new ATestUserRole();
	role.setRoleName("createRoleWithSameName");
	roleManager.createRole(role);

	assertNotNull(roleManager.getRoleByName("createRoleWithSameName"));

	ATestUserRole role2 = new ATestUserRole();
	role2.setRoleName("createRoleWithSameName");
	try {
	    roleManager.createRole(role2);
	    fail("The role name is the same thus this item must not be created");
	} catch (ItemExistsException e) {
	    assertEquals("Wrong field info", "ROLENAME", e.getFieldName());
	    assertEquals("Wrong class info", "UserRole", e.getClassName());
	} finally {
	    FACTORY.releaseSession(brokenSession);
	}

	roleManager = FACTORY.getDAOManager().getRoleDAO(session);
	assertNull("Transaction rolback test", roleManager.getRoleByName("createRoleWithSameID"));
    }

    @Test
    public void createPermissionWithSameName() throws DataConstraintException, OperationException {

	Session brokenSession = TUtil.createTestSession();

	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserPermission perm = new ATestUserPermission();
	perm.setPermissionName("createPermissionWithSameName");
	roleManager.createPermission(perm);

	assertNotNull(roleManager.getPermissionByName("createPermissionWithSameName"));

	ATestUserPermission perm2 = new ATestUserPermission();
	perm2.setPermissionName("createPermissionWithSameName");
	try {
	    roleManager.createPermission(perm2);
	    fail("The permission name is the same thus this item must not be created");
	} catch (ItemExistsException e) {
	    assertEquals("Wrong field info", "PERMISSIONNAME", e.getFieldName());
	    assertEquals("Wrong class info", "UserPermission", e.getClassName());
	} finally {
	    FACTORY.releaseSession(brokenSession);
	}

	roleManager = FACTORY.getDAOManager().getRoleDAO(session);
	assertNull("Transaction rolback test", roleManager.getRoleByName("createRoleWithSameID"));
    }

    @Test
    public void testNullRoleName() throws DataConstraintException, OperationException {
	Session brokenSession = TUtil.createTestSession();

	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserRole role = new ATestUserRole();

	try {
	    roleManager.createRole(role);
	    fail("the rolename was empty; thus this item must not be created");
	} catch (FieldRequiredException e) {
	    assertEquals("Wrong field info", "roleName", e.getFieldName());
	} finally {
	    FACTORY.releaseSession(brokenSession);
	}

    }

    @Test
    public void testNullPermissionName() throws DataConstraintException, OperationException {
	Session brokenSession = TUtil.createTestSession();

	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserPermission perm = new ATestUserPermission();

	try {
	    roleManager.createPermission(perm);
	    fail("the permission name was empty; thus this item must not be created");
	} catch (FieldRequiredException e) {
	    assertEquals("Wrong field info", "permissionName", e.getFieldName());
	} finally {
	    FACTORY.releaseSession(brokenSession);
	}

    }

    @Test
    public void testNullPermissionAssignment() throws DataConstraintException, OperationException {
	Session brokenSession = TUtil.createTestSession();

	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserRole role = new ATestUserRole();
	role.setRoleName("testNullPermissionAssignment");
	roleManager.createRole(role);
	assertNotNull(roleManager.getRoleByName("testNullPermissionAssignment"));

	try {
	    roleManager.assignPermissionToRole(role, null);
	    fail("the permission was null; thus this item must not be assigned");
	} catch (ItemNotFoundException e) {
	    assertEquals("wrong class info", "UserPermission", e.getClassName());
	} finally {
	    FACTORY.getSessionManager().releaseSession(brokenSession);
	}
    }

    @Test
    public void testNullRoleAssignment() throws DataConstraintException, OperationException {
	Session brokenSession = TUtil.createTestSession();

	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserPermission perm = new ATestUserPermission();
	perm.setPermissionName("testNullRoleAssignment");
	roleManager.createPermission(perm);
	assertNotNull(roleManager.getPermissionByName("testNullRoleAssignment"));

	try {
	    roleManager.assignPermissionToRole(null, perm);
	    fail("the permission was null; thus this item must not be assigned");
	} catch (ItemNotFoundException e) {
	    assertEquals("wrong class info", "UserRole", e.getClassName());
	} finally {
	    FACTORY.getSessionManager().releaseSession(brokenSession);
	}
    }

    // TODO: create test for assign null and to null

}
