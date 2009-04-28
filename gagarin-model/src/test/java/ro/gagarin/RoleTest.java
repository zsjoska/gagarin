package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ro.gagarin.exceptions.AlreadyExistsException;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

/**
 * Unit test for simple App.
 */
public class RoleTest {

	private Session session = new Session();

	@Test
	public void createGetDeleteSimpleRole() throws AlreadyExistsException {
		RoleManager roleManager = ModelFactory.getRoleManager(session);
		UserRole role = new UserRole();
		role.setRoleName("A_ROLE");
		roleManager.createRole(role);

		UserRole role2 = roleManager.getRoleByName("A_ROLE");
		assertNotNull(role2);
		assertEquals(role.getId(), role2.getId());
		assertEquals(role.getRoleName(), role2.getRoleName());

		roleManager.deleteRole(role2);
		role2 = roleManager.getRoleByName("A_ROLE");
		assertNull(role2);
		roleManager.release();
	}

	@Test
	public void createGetDeleteSimplePermission() throws AlreadyExistsException {
		RoleManager roleManager = ModelFactory.getRoleManager(session);

		UserPermission perm = new UserPermission();
		perm.setPermissionName("A_PERMISSION");
		roleManager.createPermission(perm);

		UserPermission perm2 = roleManager.getPermissionByName("A_PERMISSION");
		assertNotNull(perm2);
		assertEquals(perm.getId(), perm2.getId());
		assertEquals(perm.getPermissionName(), perm2.getPermissionName());

		roleManager.deletePermission(perm);
		perm2 = roleManager.getPermissionByName("A_PERMISSION");
		assertNull(perm2);
		roleManager.release();
	}

	@Test
	public void createRoleWithPermission() throws AlreadyExistsException {
		RoleManager roleManager = ModelFactory.getRoleManager(session);
		UserRole role = new UserRole();
		role.setRoleName("C_ROLE");

		UserPermission perm = new UserPermission();
		perm.setPermissionName("C_PERMISSION");

		role.getUserPermissions().add(perm);
		perm.getUserRoles().add(role);

		roleManager.createPermission(perm);
		roleManager.createRole(role);

		UserRole role2 = roleManager.getRoleByName("C_ROLE");
		assertNotNull(role2);
		assertNotNull(role2.getUserPermissions());
		assertEquals(role2.getUserPermissions().size(), 1);

		UserPermission perm2 = roleManager.getPermissionByName("C_PERMISSION");
		assertNotNull(perm2);
		assertNotNull(perm2.getUserRoles());
		assertEquals(perm2.getUserRoles().size(), 1);

		roleManager.deleteRole(role);
		roleManager.deletePermission(perm);

		roleManager.release();
	}

	@Test
	public void createRoleWith2Permissions() throws AlreadyExistsException {
		RoleManager roleManager = ModelFactory.getRoleManager(session);
		UserRole role = new UserRole();
		role.setRoleName("C_ROLE");

		UserPermission perm1 = new UserPermission();
		perm1.setPermissionName("C_PERMISSION1");
		UserPermission perm2 = new UserPermission();
		perm2.setPermissionName("C_PERMISSION2");

		role.getUserPermissions().add(perm1);
		role.getUserPermissions().add(perm2);
		perm1.getUserRoles().add(role);
		perm2.getUserRoles().add(role);

		roleManager.createPermission(perm1);
		roleManager.createPermission(perm2);
		roleManager.createRole(role);

		UserRole role2 = roleManager.getRoleByName("C_ROLE");
		assertNotNull(role2);
		assertNotNull(role2.getUserPermissions());
		assertEquals(role2.getUserPermissions().size(), 2);

		UserPermission perm_1 = roleManager.getPermissionByName("C_PERMISSION1");
		assertNotNull(perm_1);
		assertNotNull(perm_1.getUserRoles());
		assertEquals(1, perm_1.getUserRoles().size());

		UserPermission perm_2 = roleManager.getPermissionByName("C_PERMISSION2");
		assertNotNull(perm_2);
		assertNotNull(perm_2.getUserRoles());
		assertEquals(1, perm_2.getUserRoles().size());

		roleManager.deleteRole(role);
		roleManager.deletePermission(perm1);
		roleManager.deletePermission(perm2);

		roleManager.release();
	}

	@Test
	public void addPermissionToRole() throws AlreadyExistsException {
		RoleManager roleManager = ModelFactory.getRoleManager(session);
		UserRole role = roleManager.getRoleByName("B_ROLE");
		if (role == null) {
			role = new UserRole();
			role.setRoleName("B_ROLE");
			roleManager.createRole(role);
		}

		System.out.println("Permissions:" + role.getUserPermissions().size());

		UserPermission permission = new UserPermission();
		permission.setPermissionName("PERM" + role.getUserPermissions().size());

		role.getUserPermissions().add(permission);
		permission.getUserRoles().add(role);

		roleManager.release();
	}
}
