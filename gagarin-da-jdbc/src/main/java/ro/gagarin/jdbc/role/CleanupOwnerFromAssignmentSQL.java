package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.Owner;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.utils.FieldValidator;

/**
 * Removes all references to this owner from the assignment table
 * 
 * @author ZsJoska
 * 
 */
public class CleanupOwnerFromAssignmentSQL extends UpdateQuery {

    private final Owner owner;

    public CleanupOwnerFromAssignmentSQL(BaseJdbcDAO dao, Owner owner) {
	super(dao);
	this.owner = owner;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", owner);

    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, owner.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM RoleOwnerAssignment WHERE owner_id = ?";
    }

}
