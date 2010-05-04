package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.management.relation.Role;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

public class AssignPermissionToRoleSQL extends UpdateQuery {

    private final UserRole role;
    private final UserPermission perm;

    public AssignPermissionToRoleSQL(BaseJdbcDAO dao, UserRole role, UserPermission perm) {
	super(dao, Role.class);
	this.role = role;
	this.perm = perm;
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, role.getId());
	stmnt.setLong(2, perm.getId());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO PermissionAssignment( role_id, perm_id) VALUES (?,?)";
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", role);
	FieldValidator.requireLongField("id", perm);
    }

}
