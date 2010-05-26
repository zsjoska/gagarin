package ro.gagarin.jdbc.group;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;

public class UnassignUserFromGroupSQL extends UpdateQuery {

    private final User usr;
    private final Group gr;

    public UnassignUserFromGroupSQL(BaseJdbcDAO dao, User usr, Group gr) {
	super(dao);
	this.usr = usr;
	this.gr = gr;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", usr);
	FieldValidator.requireLongField("id", gr);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, usr.getId());
	stmnt.setLong(2, gr.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM UserGroupAssignment WHERE user_id = ? and group_id = ?";
    }

}
