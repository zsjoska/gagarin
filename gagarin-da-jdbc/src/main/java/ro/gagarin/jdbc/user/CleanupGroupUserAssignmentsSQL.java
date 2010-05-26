package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.Group;
import ro.gagarin.utils.FieldValidator;

/**
 * Removes all references to this group from the UserGroupAssignment table
 * 
 * @author ZsJoska
 * 
 */
public class CleanupGroupUserAssignmentsSQL extends UpdateQuery {

    private final Group group;

    public CleanupGroupUserAssignmentsSQL(BaseJdbcDAO dao, Group group) {
	super(dao);
	this.group = group;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", group);

    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.group.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM UserGroupAssignment WHERE group_id = ?";
    }

}
