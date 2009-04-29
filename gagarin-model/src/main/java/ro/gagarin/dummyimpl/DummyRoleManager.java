package ro.gagarin.dummyimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ro.gagarin.RoleManager;
import ro.gagarin.user.DBUserPermission;
import ro.gagarin.user.DBUserRole;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class DummyRoleManager implements RoleManager {

	private static HashMap<Long, DBUserPermission> permissions_id = new HashMap<Long, DBUserPermission>();
	private static HashMap<Long, DBUserRole> roles_id = new HashMap<Long, DBUserRole>();
	private static HashMap<String, DBUserPermission> permissions_name = new HashMap<String, DBUserPermission>();
	private static HashMap<String, DBUserRole> roles_name = new HashMap<String, DBUserRole>();

	@Override
	public long createPermission(DBUserPermission perm) {
		DummyRoleManager.permissions_id.put(perm.getId(), perm);
		DummyRoleManager.permissions_name.put(perm.getPermissionName(), perm);
		return perm.getId();
	}

	@Override
	public long createRole(DBUserRole role) {
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
	public List<DBUserPermission> getAllPermissions() {
		ArrayList<DBUserPermission> permissions = new ArrayList<DBUserPermission>();
		for (DBUserPermission userPermission : DummyRoleManager.permissions_id.values()) {
			permissions.add(userPermission);
		}
		return permissions;
	}

	@Override
	public UserPermission getPermissionByName(String string) {
		return DummyRoleManager.permissions_name.get(string);
	}

	@Override
	public DBUserRole getRoleByName(String roleName) {
		return DummyRoleManager.roles_name.get(roleName);
	}

	@Override
	public void release() {
	}

	@Override
	public ArrayList<DBUserPermission> substractUsersRolePermissions(UserRole main,
			UserRole substract) {

		ArrayList<DBUserPermission> leftPermissions = new ArrayList<DBUserPermission>();
		UserRole mainRole = DummyRoleManager.roles_id.get(main.getId());
		UserRole substractRole = DummyRoleManager.roles_id.get(substract.getId());

		Iterator<? extends UserPermission> iterator = mainRole.getUserPermissions().iterator();
		Set<? extends UserPermission> subPerm = substractRole.getUserPermissions();
		while (iterator.hasNext()) {
			DBUserPermission userPermission = (DBUserPermission) iterator.next();
			if (!subPerm.contains(userPermission)) {
				leftPermissions.add(userPermission);
			}
		}
		return leftPermissions;
	}

	@Override
	public List<DBUserRole> getAllRoles() {
		ArrayList<DBUserRole> roles = new ArrayList<DBUserRole>();
		for (DBUserRole userPermission : DummyRoleManager.roles_id.values()) {
			roles.add(userPermission);
		}
		return roles;
	}
}
