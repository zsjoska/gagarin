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

    public AssignRoleToPersonSQL(BaseJdbcDAO dao, UserRole role, Person person, ControlEntity object) {
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
	FieldValidator.requireField("type", this.person);
	FieldValidator.requireField("category", this.object);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.role.getId());
	stmnt.setLong(2, this.person.getId());
	stmnt.setString(3, this.person.getType().name());
	stmnt.setLong(4, this.object.getId());
	stmnt.setString(5, this.object.getCategory().name());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO RolePersonAssignment (role_id, person_id, person_type, object_id, object_type) VALUES ( ?, ?, ?, ?, ?)";
    }
}
