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
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestUser;
import ro.gagarin.testobjects.ATestUserPermission;
import ro.gagarin.testobjects.ATestUserRole;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

/**
 * Unit test for simple App.
 */
public class RoleTest {

	private Session session = null;

	@Before
	public void init() {
		this.session = BasicManagerFactory.getInstance().getSessionManager().createSession(null,
				null, BasicManagerFactory.getInstance());
	}

	@After
	public void close() {
		BasicManagerFactory.getInstance().releaseSession(session);
	}

	@Test
	public void createGetDeleteSimpleRole() throws DataConstraintException {
		RoleDAO roleManager = BasicManagerFactory.getInstance().getDAOManager().getRoleDAO(session);
		ATestUserRole role = new ATestUserRole();
		role.setRoleName("A_ROLE");
		roleManager.createRole(role);

		UserRole role2 = roleManager.getRoleByName("A_ROLE");
		assertNotNull(role2);
		assertEquals(role.getId(), role2.getId());
		assertEquals(role.getRoleName(), role2.getRoleName());

		roleManager.deleteRole(role2);
		role2 = roleManager.getRoleByName("A_ROLE");
		assertNull(role2);
	}

	@Test
	public void createGetDeleteSimplePermission() throws DataConstraintException {
		RoleDAO roleManager = BasicManagerFactory.getInstance().getDAOManager().getRoleDAO(session);

		ATestUserPermission perm = new ATestUserPermission();
		perm.setPermissionName("A_PERMISSION");
		roleManager.createPermission(perm);

		UserPermission perm2 = roleManager.getPermissionByName("A_PERMISSION");
		assertNotNull(perm2);
		assertEquals(perm.getId(), perm2.getId());
		assertEquals(perm.getPermissionName(), perm2.getPermissionName());

		roleManager.deletePermission(perm);
		perm2 = roleManager.getPermissionByName("A_PERMISSION");
		assertNull(perm2);
	}

	@Test
	public void createRoleWithPermission() throws ItemNotFoundException, DataConstraintException {
		RoleDAO roleManager = BasicManagerFactory.getInstance().getDAOManager().getRoleDAO(session);
		ATestUserRole role = new ATestUserRole();
		role.setRoleName("C_ROLE");

		ATestUserPermission perm = new ATestUserPermission();
		perm.setPermissionName("C_PERMISSION");

		roleManager.createPermission(perm);
		roleManager.createRole(role);
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
	public void createRoleWith2Permissions() throws ItemNotFoundException, DataConstraintException {
		RoleDAO roleManager = BasicManagerFactory.getInstance().getDAOManager().getRoleDAO(session);
		ATestUserRole role = new ATestUserRole();
		role.setRoleName("C_ROLE");

		ATestUserPermission perm1 = new ATestUserPermission();
		perm1.setPermissionName("C_PERMISSION1");
		ATestUserPermission perm2 = new ATestUserPermission();
		perm2.setPermissionName("C_PERMISSION2");

		roleManager.createPermission(perm1);
		roleManager.createPermission(perm2);
		roleManager.createRole(role);

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
	public void addPermissionToRole() throws ItemNotFoundException, DataConstraintException {
		RoleDAO roleManager = BasicManagerFactory.getInstance().getDAOManager().getRoleDAO(session);
		UserRole role = roleManager.getRoleByName("B_ROLE");
		if (role == null) {
			ATestUserRole role2 = new ATestUserRole();
			role2.setRoleName("B_ROLE");
			roleManager.createRole(role2);
			role = role2;
		}

		Set<UserPermission> rolePermissions = roleManager.getRolePermissions(role);

		ATestUserPermission permission = new ATestUserPermission();
		permission.setPermissionName("PERM" + rolePermissions.size());

		roleManager.createPermission(permission);
		roleManager.assignPermissionToRole(role, permission);
	}

	@Test
	public void testGetUsersWithRole() throws DataConstraintException {
		ManagerFactory factory = BasicManagerFactory.getInstance();
		RoleDAO roleManager = factory.getDAOManager().getRoleDAO(session);
		UserDAO userDAO = factory.getDAOManager().getUserDAO(session);

		ATestUserRole role = new ATestUserRole();
		role.setRoleName("A_ROLE");
		roleManager.createRole(role);

		ATestUser user = new ATestUser();
		user.setName("name");
		user.setUsername("username");
		user.setPassword("password");
		user.setRole(role);
		userDAO.createUser(user);

		List<User> usersWithRole = userDAO.getUsersWithRole(role);

		assertNotNull(usersWithRole);
		assertEquals(1, usersWithRole.size());
		assertEquals(user.getId(), usersWithRole.get(0).getId());

		userDAO.deleteUser(user);
		roleManager.deleteRole(role);
	}

	@Test
	public void createRoleWithSameID() throws DataConstraintException {
		ManagerFactory factory = BasicManagerFactory.getInstance();

		Session brokenSession = factory.getSessionManager().createSession(null, null,
				BasicManagerFactory.getInstance());

		RoleDAO roleManager = factory.getDAOManager().getRoleDAO(brokenSession);

		ATestUserRole role = new ATestUserRole();
		role.setRoleName("createRoleWithSameID");
		roleManager.createRole(role);

		assertNotNull(roleManager.getRoleByName("createRoleWithSameID"));

		ATestUserRole role2 = new ATestUserRole();
		role2.setRoleName("createRoleWithSameID2");
		role2.setId(role.getId());
		try {
			roleManager.createRole(role2);
			fail("The role id is the same thus this item must not be created");
		} catch (ItemExistsException e) {
			assertEquals("Wrong field info", "ID", e.getFieldName());
			assertEquals("Wrong class info", "UserRole", e.getClassName());
		} finally {
			factory.releaseSession(brokenSession);
		}

		roleManager = factory.getDAOManager().getRoleDAO(session);
		assertNull("Transaction rolback test", roleManager.getRoleByName("createRoleWithSameID"));
	}

	@Test
	public void createRoleWithSameName() throws DataConstraintException {
		ManagerFactory factory = BasicManagerFactory.getInstance();

		Session brokenSession = factory.getSessionManager().createSession(null, null,
				BasicManagerFactory.getInstance());

		RoleDAO roleManager = factory.getDAOManager().getRoleDAO(brokenSession);

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
			factory.releaseSession(brokenSession);
		}

		roleManager = factory.getDAOManager().getRoleDAO(session);
		assertNull("Transaction rolback test", roleManager.getRoleByName("createRoleWithSameID"));
	}

	@Test
	public void createPermissionWithSameID() throws DataConstraintException {
		ManagerFactory factory = BasicManagerFactory.getInstance();

		Session brokenSession = factory.getSessionManager().createSession(null, null,
				BasicManagerFactory.getInstance());

		RoleDAO roleManager = factory.getDAOManager().getRoleDAO(brokenSession);

		ATestUserPermission perm = new ATestUserPermission();
		perm.setPermissionName("createPermissionWithSameID");
		roleManager.createPermission(perm);

		assertNotNull(roleManager.getPermissionByName("createPermissionWithSameID"));

		ATestUserPermission perm2 = new ATestUserPermission();
		perm2.setPermissionName("createPermissionWithSameID2");
		perm2.setId(perm.getId());
		try {
			roleManager.createPermission(perm2);
			fail("The permission id is the same thus this item must not be created");
		} catch (ItemExistsException e) {
			assertEquals("Wrong field info", "ID", e.getFieldName());
			assertEquals("Wrong class info", "UserPermission", e.getClassName());
		} finally {
			factory.releaseSession(brokenSession);
		}

		roleManager = factory.getDAOManager().getRoleDAO(session);
		assertNull("Transaction rolback test", roleManager.getRoleByName("createRoleWithSameID"));
	}

	@Test
	public void createPermissionWithSameName() throws DataConstraintException {
		ManagerFactory factory = BasicManagerFactory.getInstance();

		Session brokenSession = factory.getSessionManager().createSession(null, null,
				BasicManagerFactory.getInstance());

		RoleDAO roleManager = factory.getDAOManager().getRoleDAO(brokenSession);

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
			factory.releaseSession(brokenSession);
		}

		roleManager = factory.getDAOManager().getRoleDAO(session);
		assertNull("Transaction rolback test", roleManager.getRoleByName("createRoleWithSameID"));
	}

	// TODO: create tests for empty values
}
