package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.exceptions.FieldRequiredException;
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
	public void createGetDeleteSimpleRole() {
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
	public void createGetDeleteSimplePermission() {
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
	public void createRoleWithPermission() throws ItemNotFoundException {
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
	public void createRoleWith2Permissions() throws ItemNotFoundException {
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
		assertNotNull(role2.getUserPermissions());
		assertEquals(2, role2.getUserPermissions().size());

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
	public void addPermissionToRole() throws ItemNotFoundException {
		RoleDAO roleManager = BasicManagerFactory.getInstance().getDAOManager().getRoleDAO(session);
		UserRole role = roleManager.getRoleByName("B_ROLE");
		if (role == null) {
			ATestUserRole role2 = new ATestUserRole();
			role2.setRoleName("B_ROLE");
			roleManager.createRole(role2);
			role = role2;
		}

		System.out.println("Permissions:" + role.getUserPermissions().size());

		ATestUserPermission permission = new ATestUserPermission();
		permission.setPermissionName("PERM" + role.getUserPermissions().size());

		roleManager.createPermission(permission);
		roleManager.assignPermissionToRole(role, permission);
	}

	@Test
	public void testGetUsersWithRole() throws FieldRequiredException, ItemNotFoundException,
			ItemExistsException {
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

		userDAO.deleteUserById(user.getId());
		roleManager.deleteRole(role);
	}
}
