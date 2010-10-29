package ro.gagarin.generictable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;

public class DeleteGenericRecordSQL extends UpdateQuery {

    private final String tableName;
    private final Long id;

    public DeleteGenericRecordSQL(Long id, BaseJdbcDAO dao, String tableName) {
	super(dao);
	this.id = id;
	this.tableName = tableName;
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM " + tableName + " WHERE id = ?";
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.id);
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// TODO Auto-generated method stub
    }
}
