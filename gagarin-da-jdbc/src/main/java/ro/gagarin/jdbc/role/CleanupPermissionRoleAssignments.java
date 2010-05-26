package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.FieldValidator;

/**
 * Removes all references from the PermissionAssignment table for the given
 * {@link UserPermission}
 * 
 * @author ZsJoska
 * 
 */
public class CleanupPermissionRoleAssignments extends UpdateQuery {

    private final UserPermission perm;

    public CleanupPermissionRoleAssignments(BaseJdbcDAO dao, UserPermission perm) {
	super(dao);
	this.perm = perm;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", perm);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.perm.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM PermissionAssignment WHERE perm_id = ?";
    }

}
