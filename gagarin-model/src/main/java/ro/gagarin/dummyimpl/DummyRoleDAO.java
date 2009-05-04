package ro.gagarin.dummyimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ro.gagarin.RoleDAO;
import ro.gagarin.hibernate.objects.DBUserPermission;
import ro.gagarin.hibernate.objects.DBUserRole;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class DummyRoleDAO implements RoleDAO {

	private static HashMap<Long, DBUserPermission> permissions_id = new HashMap<Long, DBUserPermission>();
	private static HashMap<Long, DBUserRole> roles_id = new HashMap<Long, DBUserRole>();
	private static HashMap<String, DBUserPermission> permissions_name = new HashMap<String, DBUserPermission>();
	private static HashMap<String, DBUserRole> roles_name = new HashMap<String, DBUserRole>();

	@Override
	public long createPermission(DBUserPermission perm) {
		DummyRoleDAO.permissions_id.put(perm.getId(), perm);
		DummyRoleDAO.permissions_name.put(perm.getPermissionName(), perm);
		return perm.getId();
	}

	@Override
	public long createRole(DBUserRole role) {
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
	public List<DBUserPermission> getAllPermissions() {
		ArrayList<DBUserPermission> permissions = new ArrayList<DBUserPermission>();
		for (DBUserPermission userPermission : DummyRoleDAO.permissions_id.values()) {
			permissions.add(userPermission);
		}
		return permissions;
	}

	@Override
	public UserPermission getPermissionByName(String string) {
		return DummyRoleDAO.permissions_name.get(string);
	}

	@Override
	public DBUserRole getRoleByName(String roleName) {
		return DummyRoleDAO.roles_name.get(roleName);
	}

	@Override
	public void release() {
	}

	@Override
	public ArrayList<DBUserPermission> substractUsersRolePermissions(UserRole main,
			UserRole substract) {

		ArrayList<DBUserPermission> leftPermissions = new ArrayList<DBUserPermission>();
		UserRole mainRole = DummyRoleDAO.roles_id.get(main.getId());
		UserRole substractRole = DummyRoleDAO.roles_id.get(substract.getId());

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
		for (DBUserRole userPermission : DummyRoleDAO.roles_id.values()) {
			roles.add(userPermission);
		}
		return roles;
	}
}
