package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.ControlEntity;
import ro.gagarin.Person;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

public class UnAssignRoleFromPersonSQL extends UpdateQuery {

    private final UserRole role;
    private final Person person;
    private final ControlEntity object;

    public UnAssignRoleFromPersonSQL(BaseJdbcDAO dao, UserRole role, Person person, ControlEntity object) {
	super(dao, role.getClass());
	this.role = role;
	this.person = person;
	this.object = object;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", this.role);
	FieldValidator.requireLongField("id", this.person);
	FieldValidator.requireLongField("id", this.object);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.role.getId());
	stmnt.setLong(2, this.person.getId());
	stmnt.setLong(3, this.object.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM RolePersonAssignment WHERE role_id = ? AND person_id = ? AND object_id = ?";
    }
}
