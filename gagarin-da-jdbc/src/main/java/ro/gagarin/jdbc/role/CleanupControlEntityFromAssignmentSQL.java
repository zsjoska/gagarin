package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.ControlEntity;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.utils.FieldValidator;

/**
 * Removes all references to this control entity from the assignment table
 * 
 * @author ZsJoska
 * 
 */
public class CleanupControlEntityFromAssignmentSQL extends UpdateQuery {

    private final ControlEntity ce;

    public CleanupControlEntityFromAssignmentSQL(BaseJdbcDAO dao, ControlEntity ce) {
	super(dao, ce.getClass());
	this.ce = ce;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", ce);

    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, ce.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM RolePersonAssignment WHERE object_id = ?";
    }

}
