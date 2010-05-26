package ro.gagarin.jdbc.group;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.Group;
import ro.gagarin.utils.FieldValidator;

public class DeleteGroupSQL extends UpdateQuery {

    private final Group group;

    public DeleteGroupSQL(BaseJdbcDAO dao, Group group) {
	super(dao);
	this.group = group;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", group);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, group.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE from Groups where id = ?";
    }
}
