package ro.gagarin.dummyimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ro.gagarin.RoleDAO;
import ro.gagarin.dummyimpl.objects.DUserPermission;
import ro.gagarin.dummyimpl.objects.DUserRole;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class DummyRoleDAO extends DummyBase implements RoleDAO {

	private static HashMap<Long, DUserPermission> permissions_id = new HashMap<Long, DUserPermission>();
	private static HashMap<Long, DUserRole> roles_id = new HashMap<Long, DUserRole>();
	private static HashMap<String, DUserPermission> permissions_name = new HashMap<String, DUserPermission>();
	private static HashMap<String, UserRole> roles_name = new HashMap<String, UserRole>();

	@Override
	public long createPermission(UserPermission perm) {
		DUserPermission userPerm = new DUserPermission(perm);
		DummyRoleDAO.permissions_id.put(perm.getId(), userPerm);
		DummyRoleDAO.permissions_name.put(perm.getPermissionName(), userPerm);
		return perm.getId();
	}

	@Override
	public long createRole(UserRole role) {
		DummyRoleDAO.roles_id.put(role.getId(), new DUserRole(role));
		DummyRoleDAO.roles_name.put(role.getRoleName(), role);
		return role.getId();
	}

	@Override
	public void deletePermission(UserPermission perm) {
		DummyRoleDAO.permissions_id.remove(perm.getId());
		DummyRoleDAO.permissions_name.remove(perm.getPermissionName());
	}

	@Override
	public void deleteRole(UserRole role2) {
		DummyRoleDAO.roles_id.remove(role2.getId());
		DummyRoleDAO.roles_name.remove(role2.getRoleName());
	}

	@Override
	public List<UserPermission> getAllPermissions() {
		ArrayList<UserPermission> permissions = new ArrayList<UserPermission>();
		for (UserPermission userPermission : DummyRoleDAO.permissions_id.values()) {
			permissions.add(userPermission);
		}
		return permissions;
	}

	@Override
	public UserPermission getPermissionByName(String string) {
		return DummyRoleDAO.permissions_name.get(string);
	}

	@Override
	public UserRole getRoleByName(String roleName) {
		return DummyRoleDAO.roles_name.get(roleName);
	}

	@Override
	public void release() {
	}

	@Override
	public ArrayList<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract) {

		ArrayList<UserPermission> leftPermissions = new ArrayList<UserPermission>();
		UserRole mainRole = DummyRoleDAO.roles_id.get(main.getId());
		UserRole substractRole = DummyRoleDAO.roles_id.get(substract.getId());

		Iterator<? extends UserPermission> iterator = mainRole.getUserPermissions().iterator();
		Set<? extends UserPermission> subPerm = substractRole.getUserPermissions();
		while (iterator.hasNext()) {
			UserPermission userPermission = (UserPermission) iterator.next();
			if (!subPerm.contains(userPermission)) {
				leftPermissions.add(userPermission);
			}
		}
		return leftPermissions;
	}

	@Override
	public List<UserRole> getAllRoles() {
		ArrayList<UserRole> roles = new ArrayList<UserRole>();
		for (UserRole userPermission : DummyRoleDAO.roles_id.values()) {
			roles.add(userPermission);
		}
		return roles;
	}

	@Override
	public void assignPermissionToRole(UserRole role, UserPermission perm) {
		DUserRole userRole = roles_id.get(role.getId());
		DUserPermission userPerm = permissions_id.get(perm.getId());

		Set<UserPermission> userPermissions = userRole.getUserPermissions();
		if (userPermissions == null) {
			userPermissions = new HashSet<UserPermission>();
			userRole.setUserPermissions(userPermissions);
		}
		userPermissions.add(userPerm);

		Set<UserRole> userRoles = perm.getUserRoles();
		if (userRoles == null) {
			userRoles = new HashSet<UserRole>();
			userPerm.setUserRoles(userRoles);
		}
		userRoles.add(userRole);
	}
}