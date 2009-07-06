package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class SubstractRolesPermissions extends SelectQuery {

	private final UserRole main;
	private final UserRole sub;
	private List<UserPermission> perm = null;

	public SubstractRolesPermissions(BaseJdbcDAO dao, UserRole main, UserRole sub) {
		super(dao, UserRole.class);
		this.main = main;
		this.sub = sub;
	}

	@Override
	protected void useResult(ResultSet rs) throws SQLException {
		this.perm = new ArrayList<UserPermission>();
		while (rs.next()) {
			DBUserPermission role = new DBUserPermission();
			role.setId(rs.getLong("id"));
			role.setPermissionName(rs.getString("permissionName"));
			perm.add(role);
		}
	}

	@Override
	protected void fillParameters(PreparedStatement stmnt) throws SQLException {
		stmnt.setLong(1, main.getId());
		stmnt.setLong(2, sub.getId());
	}

	@Override
	protected String getSQL() {
		return "SELECT id, permissionName FROM UserPermissions INNER JOIN PermissionAssignment ON UserPermissions.id = PermissionAssignment.perm_id WHERE PermissionAssignment.role_id = ? AND id NOT IN "
				+ "("
				+ "SELECT id FROM UserPermissions INNER JOIN PermissionAssignment ON UserPermissions.id = PermissionAssignment.perm_id WHERE PermissionAssignment.role_id = ?"
				+ ")";
	}

	public static List<UserPermission> execute(BaseJdbcDAO dao, UserRole main, UserRole sub)
			throws OperationException {
		SubstractRolesPermissions q = new SubstractRolesPermissions(dao, main, sub);
		q.execute();
		return q.perm;
	}

}
