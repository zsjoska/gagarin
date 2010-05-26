package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

public class GetRolePermissionsSQL extends SelectQuery {

    private Set<UserPermission> permissions = null;
    private final UserRole role;

    public GetRolePermissionsSQL(BaseJdbcDAO dao, UserRole role) {
	super(dao);
	this.role = role;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	permissions = new HashSet<UserPermission>();
	while (rs.next()) {
	    DBUserPermission permission = new DBUserPermission();
	    permission.setId(rs.getLong("id"));
	    permission.setPermissionName(rs.getString("permissionName"));
	    permissions.add(permission);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, role.getId());
    }

    @Override
    protected String getSQL() {
	return "SELECT id, permissionName FROM UserPermissions INNER JOIN PermissionAssignment ON id = perm_id WHERE role_id = ?";
    }

    public static Set<UserPermission> execute(BaseJdbcDAO dao, UserRole role) throws OperationException {
	GetRolePermissionsSQL q = new GetRolePermissionsSQL(dao, role);
	q.execute();
	return q.permissions;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", role);
    }

}
