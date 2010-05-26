package ro.gagarin.jdbc.group;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;

public class AssignUserToGroupSQL extends UpdateQuery {

    private final User user;
    private final Group group;

    public AssignUserToGroupSQL(BaseJdbcDAO dao, User user, Group group) {
	super(dao);
	this.user = user;
	this.group = group;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", user);
	FieldValidator.requireLongField("id", group);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, user.getId());
	stmnt.setLong(2, group.getId());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO UserGroupAssignment (user_id, group_id) VALUES (?,?)";
    }
}
