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
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

public class GetPermissionRolesSQL extends SelectQuery {

    private Set<UserRole> roles = null;
    private final UserPermission perm;

    public GetPermissionRolesSQL(BaseJdbcDAO dao, UserPermission perm) {
	super(dao, UserRole.class);
	this.perm = perm;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	roles = new HashSet<UserRole>();
	while (rs.next()) {
	    DBUserRole role = new DBUserRole();
	    role.setId(rs.getLong("id"));
	    role.setRoleName(rs.getString("roleName"));
	    roles.add(role);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, perm.getId());
    }

    @Override
    protected String getSQL() {
	return "SELECT id, roleName FROM UserRoles INNER JOIN PermissionAssignment ON id = role_id WHERE perm_id = ?";
    }

    public static Set<UserRole> execute(BaseJdbcDAO dao, UserPermission perm) throws OperationException {
	GetPermissionRolesSQL q = new GetPermissionRolesSQL(dao, perm);
	q.execute();
	return q.roles;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", perm);
    }
}
