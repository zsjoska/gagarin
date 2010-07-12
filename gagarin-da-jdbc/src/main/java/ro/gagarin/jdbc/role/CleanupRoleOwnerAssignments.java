package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

public class CleanupRoleOwnerAssignments extends UpdateQuery {

    private final UserRole role;

    public CleanupRoleOwnerAssignments(BaseJdbcDAO dao, UserRole role) {
	super(dao);
	this.role = role;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", role);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, role.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM RoleOwnerAssignment WHERE role_id = ?";
    }
}
