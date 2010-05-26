package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestUserPermission;
import ro.gagarin.testobjects.ATestUserRole;
import ro.gagarin.testutil.TUtil;
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
	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
	ATestUserRole role = new ATestUserRole();
	role.setRoleName("A_ROLE");
	role.setId(roleDAO.createRole(role));

	UserRole role2 = roleDAO.getRoleByName("A_ROLE");
	assertNotNull(role2);
	assertEquals(role.getId(), role2.getId());
	assertEquals(role.getRoleName(), role2.getRoleName());

	roleDAO.deleteRole(role2);
	role2 = roleDAO.getRoleByName("A_ROLE");
	assertNull(role2);
    }

    @Test
    public void createGetDeleteSimplePermission() throws Exception {
	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(session);

	ATestUserPermission perm = new ATestUserPermission();
	perm.setPermissionName("A_PERMISSION");
	perm.setId(roleDAO.createPermission(perm));

	UserPermission perm2 = roleDAO.getPermissionByName("A_PERMISSION");
	assertNotNull(perm2);
	assertEquals(perm.getId(), perm2.getId());
	assertEquals(perm.getPermissionName(), perm2.getPermissionName());

	roleDAO.deletePermission(perm);
	perm2 = roleDAO.getPermissionByName("A_PERMISSION");
	assertNull(perm2);
    }

    @Test
    public void createRoleWithPermission() throws ItemNotFoundException, DataConstraintException, OperationException {
	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
	ATestUserRole role = new ATestUserRole();
	role.setRoleName("C_ROLE");

	ATestUserPermission perm = new ATestUserPermission();
	perm.setPermissionName("C_PERMISSION");

	perm.setId(roleDAO.createPermission(perm));
	role.setId(roleDAO.createRole(role));
	roleDAO.assignPermissionToRole(role, perm);

	UserRole role2 = roleDAO.getRoleByName("C_ROLE");
	assertNotNull(role2);
	Set<UserPermission> perms = roleDAO.getRolePermissions(role2);
	assertNotNull(perms);
	assertEquals(1, perms.size());

	UserPermission perm2 = roleDAO.getPermissionByName("C_PERMISSION");
	assertNotNull(perm2);
	Set<UserRole> roles = roleDAO.getPermissionRoles(perm2);
	assertNotNull(roles);
	assertEquals(1, roles.size());

	roleDAO.deleteRole(role);
	roleDAO.deletePermission(perm);

    }

    @Test
    public void createRoleWith2Permissions() throws ItemNotFoundException, DataConstraintException, OperationException {
	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
	ATestUserRole role = new ATestUserRole();
	role.setRoleName("C_ROLE");

	ATestUserPermission perm1 = new ATestUserPermission();
	perm1.setPermissionName("C_PERMISSION1");
	ATestUserPermission perm2 = new ATestUserPermission();
	perm2.setPermissionName("C_PERMISSION2");

	perm1.setId(roleDAO.createPermission(perm1));
	perm2.setId(roleDAO.createPermission(perm2));
	role.setId(roleDAO.createRole(role));

	roleDAO.assignPermissionToRole(role, perm1);
	roleDAO.assignPermissionToRole(role, perm2);

	UserRole role2 = roleDAO.getRoleByName("C_ROLE");
	assertNotNull(role2);

	Set<UserPermission> permissions = roleDAO.getRolePermissions(role2);
	assertNotNull(permissions);
	assertEquals(2, permissions.size());

	UserPermission perm_1 = roleDAO.getPermissionByName("C_PERMISSION1");
	assertNotNull(perm_1);

	Set<UserRole> roles_1 = roleDAO.getPermissionRoles(perm_1);
	assertNotNull(roles_1);
	assertEquals(1, roles_1.size());

	UserPermission perm_2 = roleDAO.getPermissionByName("C_PERMISSION2");
	assertNotNull(perm_2);
	Set<UserRole> roles_2 = roleDAO.getPermissionRoles(perm_2);
	assertNotNull(roles_2);
	assertEquals(1, roles_2.size());

	roleDAO.deleteRole(role);
	roleDAO.deletePermission(perm1);
	roleDAO.deletePermission(perm2);

    }

    @Test
    public void addPermissionToRole() throws ItemNotFoundException, DataConstraintException, OperationException {
	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
	UserRole role = roleDAO.getRoleByName("B_ROLE");
	if (role == null) {
	    ATestUserRole role2 = new ATestUserRole();
	    role2.setRoleName("B_ROLE");
	    role2.setId(roleDAO.createRole(role2));
	    role = role2;
	}

	Set<UserPermission> rolePermissions = roleDAO.getRolePermissions(role);

	ATestUserPermission permission = new ATestUserPermission();
	permission.setPermissionName("PERM" + rolePermissions.size());

	permission.setId(roleDAO.createPermission(permission));
	roleDAO.assignPermissionToRole(role, permission);
    }

    @Test
    public void createRoleWithSameName() throws DataConstraintException, OperationException, SessionNotFoundException {

	Session brokenSession = TUtil.createTestSession();

	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserRole role = new ATestUserRole();
	role.setRoleName("createRoleWithSameName");
	roleDAO.createRole(role);

	assertNotNull(roleDAO.getRoleByName("createRoleWithSameName"));

	ATestUserRole role2 = new ATestUserRole();
	role2.setRoleName("createRoleWithSameName");
	try {
	    roleDAO.createRole(role2);
	    fail("The role name is the same thus this item must not be created");
	} catch (ItemExistsException e) {
	    assertEquals("Wrong field info", "ROLENAME", e.getFieldName());
	    assertEquals("Wrong class info", "UserRole", e.getClassName());
	} finally {
	    FACTORY.releaseSession(brokenSession);
	}

	roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
	assertNull("Transaction rolback test", roleDAO.getRoleByName("createRoleWithSameID"));
    }

    @Test
    public void createPermissionWithSameName() throws DataConstraintException, OperationException {

	Session brokenSession = TUtil.createTestSession();

	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserPermission perm = new ATestUserPermission();
	perm.setPermissionName("createPermissionWithSameName");
	roleDAO.createPermission(perm);

	assertNotNull(roleDAO.getPermissionByName("createPermissionWithSameName"));

	ATestUserPermission perm2 = new ATestUserPermission();
	perm2.setPermissionName("createPermissionWithSameName");
	try {
	    roleDAO.createPermission(perm2);
	    fail("The permission name is the same thus this item must not be created");
	} catch (ItemExistsException e) {
	    assertEquals("Wrong field info", "PERMISSIONNAME", e.getFieldName());
	    assertEquals("Wrong class info", "UserPermission", e.getClassName());
	} finally {
	    FACTORY.releaseSession(brokenSession);
	}

	roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
	assertNull("Transaction rolback test", roleDAO.getRoleByName("createRoleWithSameID"));
    }

    @Test
    public void testNullRoleName() throws DataConstraintException, OperationException {
	Session brokenSession = TUtil.createTestSession();

	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserRole role = new ATestUserRole();

	try {
	    roleDAO.createRole(role);
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

	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserPermission perm = new ATestUserPermission();

	try {
	    roleDAO.createPermission(perm);
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

	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(brokenSession);

	ATestUserPermission perm = new ATestUserPermission();
	perm.setPermissionName("testNullRoleAssignment");
	roleDAO.createPermission(perm);
	assertNotNull(roleDAO.getPermissionByName("testNullRoleAssignment"));

	try {
	    roleDAO.assignPermissionToRole(null, perm);
	    fail("the permission was null; thus this item must not be assigned");
	} catch (ItemNotFoundException e) {
	    assertEquals("wrong class info", "UserRole", e.getClassName());
	} finally {
	    FACTORY.getSessionManager().releaseSession(brokenSession);
	}
    }

    // TODO:(4) create test for assign null and to null

}
