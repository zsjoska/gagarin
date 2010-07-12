package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

public class UnAssignPermissionFromRoleSQL extends UpdateQuery {

    private final UserRole role;
    private final UserPermission permission;

    public UnAssignPermissionFromRoleSQL(BaseJdbcDAO dao, UserRole r, UserPermission p) {
	super(dao);
	this.role = r;
	this.permission = p;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireIdField(role);
	FieldValidator.requireIdField(permission);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, role.getId());
	stmnt.setLong(2, permission.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM PermissionAssignment WHERE role_id =? AND perm_id = ?";
    }

}