package ro.gagarin.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ro.gagarin.RoleManager;

public class DummyRoleManager implements RoleManager {

	private static HashMap<Long, UserPermission> permissions_id = new HashMap<Long, UserPermission>();
	private static HashMap<Long, UserRole> roles_id = new HashMap<Long, UserRole>();
	private static HashMap<String, UserPermission> permissions_name = new HashMap<String, UserPermission>();
	private static HashMap<String, UserRole> roles_name = new HashMap<String, UserRole>();

	@Override
	public long createPermission(UserPermission perm) {
		DummyRoleManager.permissions_id.put(perm.getId(), perm);
		DummyRoleManager.permissions_name.put(perm.getPermissionName(), perm);
		return perm.getId();
	}

	@Override
	public long createRole(UserRole role) {
		DummyRoleManager.roles_id.put(role.getId(), role);
		DummyRoleManager.roles_name.put(role.getRoleName(), role);
		return role.getId();
	}

	@Override
	public void deletePermission(UserPermission perm) {
		DummyRoleManager.permissions_id.remove(perm.getId());
		DummyRoleManager.permissions_name.remove(perm.getPermissionName());
	}

	@Override
	public void deleteRole(UserRole role2) {
		DummyRoleManager.roles_id.remove(role2.getId());
		DummyRoleManager.roles_name.remove(role2.getRoleName());
	}

	@Override
	public List<UserPermission> getAllPermissions() {
		ArrayList<UserPermission> permissions = new ArrayList<UserPermission>();
		for (UserPermission userPermission : DummyRoleManager.permissions_id.values()) {
			permissions.add(userPermission);
		}
		return permissions;
	}

	@Override
	public UserPermission getPermissionByName(String string) {
		return DummyRoleManager.permissions_name.get(string);
	}

	@Override
	public UserRole getRoleByName(String roleName) {
		return DummyRoleManager.roles_name.get(roleName);
	}

	@Override
	public void release() {
	}

	@Override
	public ArrayList<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract) {

		ArrayList<UserPermission> leftPermissions = new ArrayList<UserPermission>();
		UserRole mainRole = DummyRoleManager.roles_id.get(main.getId());
		UserRole substractRole = DummyRoleManager.roles_id.get(substract.getId());

		Iterator<UserPermission> iterator = mainRole.getUserPermissions().iterator();
		Set<UserPermission> subPerm = substractRole.getUserPermissions();
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
		for (UserRole userPermission : DummyRoleManager.roles_id.values()) {
			roles.add(userPermission);
		}
		return roles;
	}
}
