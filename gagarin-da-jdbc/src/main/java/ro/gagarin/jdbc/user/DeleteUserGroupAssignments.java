package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;

public class DeleteUserGroupAssignments extends UpdateQuery {

    private final User user;

    public DeleteUserGroupAssignments(BaseJdbcDAO dao, User user) {
	super(dao, user.getClass());
	this.user = user;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", user);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.user.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM UserGroupAssignment WHERE user_id = ?";
    }

}
