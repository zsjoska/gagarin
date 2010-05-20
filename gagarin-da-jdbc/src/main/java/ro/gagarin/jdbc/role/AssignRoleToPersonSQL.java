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

public class AssignRoleToPersonSQL extends UpdateQuery {

    private final UserRole role;
    private final Person person;
    private final ControlEntity object;
    private final String className;

    public AssignRoleToPersonSQL(BaseJdbcDAO dao, UserRole role, Person person, String className, ControlEntity object) {
	super(dao, role.getClass());
	this.role = role;
	this.person = person;
	this.className = className;
	this.object = object;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", this.role);
	FieldValidator.requireLongField("id", this.person);
	FieldValidator.requireLongField("id", this.object);
	FieldValidator.checkStringValue(this.className, "className", 50);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.role.getId());
	stmnt.setLong(2, this.person.getId());
	stmnt.setString(3, this.className);
	stmnt.setLong(4, this.object.getId());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO RolePersonAssignment (role_id, person_id, objectType, object_id) VALUES ( ?, ?, ?, ?)";
    }
}
