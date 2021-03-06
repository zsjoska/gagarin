package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.User;

public class DeleteUserSQL extends UpdateQuery {

    private final User user;

    public DeleteUserSQL(BaseJdbcDAO dao, User user) {
	super(dao, User.class);
	this.user = user;
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, user.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM Users WHERE id = ?";
    }

}
