package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.Person;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.utils.FieldValidator;

/**
 * Removes all references to this person from the assignment table
 * 
 * @author ZsJoska
 * 
 */
public class CleanupPersonFromAssignmentSQL extends UpdateQuery {

    private final Person person;

    public CleanupPersonFromAssignmentSQL(BaseJdbcDAO dao, Person person) {
	super(dao, person.getClass());
	this.person = person;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", person);

    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, person.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM RolePersonAssignment WHERE person_id = ?";
    }

}
