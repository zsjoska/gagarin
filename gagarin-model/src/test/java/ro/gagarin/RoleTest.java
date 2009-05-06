package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.exceptions.AlreadyExistsException;
import ro.gagarin.hibernate.objects.DBUserPermission;
import ro.gagarin.hibernate.objects.DBUserRole;
import ro.gagarin.session.Session;
import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

/**
 * Unit test for simple App.
 */
public class RoleTest {

	private Session session = null;

	@Before
	public void init() {
		this.session = ModelFactory.getSessionManager().createSession(null, null);
	}

	@After
	public void close() {
		ModelFactory.releaseSession(session);
	}

	@Test
	public void createGetDeleteSimpleRole() throws AlreadyExistsException {
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);
		DBUserRole role = new DBUserRole();
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
	public void createGetDeleteSimplePermission() throws AlreadyExistsException {
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);

		DBUserPermission perm = new DBUserPermission();
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
	public void createRoleWithPermission() throws AlreadyExistsException {
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);
		DBUserRole role = new DBUserRole();
		role.setRoleName("C_ROLE");

		DBUserPermission perm = new DBUserPermission();
		perm.setPermissionName("C_PERMISSION");

		role.getUserPermissions().add(perm);
		perm.getUserRoles().add(role);

		roleManager.createRole(role);
		roleManager.createPermission(perm);

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

	}

	@Test
	public void createRoleWith2Permissions() throws AlreadyExistsException {
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);
		DBUserRole role = new DBUserRole();
		role.setRoleName("C_ROLE");

		DBUserPermission perm1 = new DBUserPermission();
		perm1.setPermissionName("C_PERMISSION1");
		DBUserPermission perm2 = new DBUserPermission();
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

	}

	@Test
	public void addPermissionToRole() throws AlreadyExistsException {
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);
		UserRole role = roleManager.getRoleByName("B_ROLE");
		if (role == null) {
			role = new UserRole() {

				private long id = BaseEntity.getNextId();

				@Override
				public String getRoleName() {
					return "B_ROLE";
				}

				@Override
				public Set<UserPermission> getUserPermissions() {
					return new HashSet<UserPermission>();
				}

				@Override
				public Long getId() {
					return this.id;
				}

			};
			roleManager.createRole(role);
		}

		System.out.println("Permissions:" + role.getUserPermissions().size());

		DBUserPermission permission = new DBUserPermission();
		permission.setPermissionName("PERM" + role.getUserPermissions().size());

		role.getUserPermissions().add(permission);
		permission.getUserRoles().add(role);

	}
}
