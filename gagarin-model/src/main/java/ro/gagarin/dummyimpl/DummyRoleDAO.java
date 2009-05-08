package ro.gagarin.dummyimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ro.gagarin.RoleDAO;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class DummyRoleDAO implements RoleDAO {

	private static HashMap<Long, UserPermission> permissions_id = new HashMap<Long, UserPermission>();
	private static HashMap<Long, UserRole> roles_id = new HashMap<Long, UserRole>();
	private static HashMap<String, UserPermission> permissions_name = new HashMap<String, UserPermission>();
	private static HashMap<String, UserRole> roles_name = new HashMap<String, UserRole>();

	@Override
	public long createPermission(UserPermission perm) {
		DummyRoleDAO.permissions_id.put(perm.getId(), perm);
		DummyRoleDAO.permissions_name.put(perm.getPermissionName(), perm);
		return perm.getId();
	}

	@Override
	public long createRole(UserRole role) {
		DummyRoleDAO.roles_id.put(role.getId(), role);
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
		roles_id.get(role.getId()).getUserPermissions().add(permissions_id.get(perm.getId()));
	}
}
