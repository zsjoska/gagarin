package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ro.gagarin.exceptions.AlreadyExistsException;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

/**
 * Unit test for simple App.
 */
public class RoleTest {

	@Test
	public void createGetDeleteSimpleRole() throws AlreadyExistsException {
		RoleManager roleManager = ModelFactory.getRoleManager();
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
		RoleManager roleManager = ModelFactory.getRoleManager();

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
		RoleManager roleManager = ModelFactory.getRoleManager();
		UserRole role = new UserRole();
		role.setRoleName("C_ROLE");

		UserPermission perm = new UserPermission();
		perm.setPermissionName("C_PERMISSION");

		role.getUserPermissions().add(perm);
		perm.getUserRoles().add(role);

		roleManager.createPermission(perm);
		// roleManager.createRole(role);

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

	// @Test
	public void addPermissionToRole() throws AlreadyExistsException {
		RoleManager roleManager = ModelFactory.getRoleManager();
		UserRole role = roleManager.getRoleByName("B_ROLE");
		if (role == null) {
			role = new UserRole();
			role.setRoleName("B_ROLE");
			roleManager.createRole(role);
		}

		Set<UserPermission> userPermissions = role.getUserPermissions();
		if (userPermissions == null) {
			userPermissions = new HashSet<UserPermission>();
		}

		System.out.println("Permissions:" + userPermissions.size());

		UserPermission permission = new UserPermission();
		permission.setPermissionName("PERM" + userPermissions.size());
		roleManager.createPermission(permission);

		userPermissions.add(permission);

		roleManager.release();
	}
}
