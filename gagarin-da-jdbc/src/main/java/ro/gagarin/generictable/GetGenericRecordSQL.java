package ro.gagarin.generictable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;

public class GetGenericRecordSQL extends SelectQuery {

    private final String tableName;
    private final long id;
    private DBGenRecord record = null;

    public GetGenericRecordSQL(long id, BaseJdbcDAO dao, String tableName) {
	super(dao);
	this.id = id;
	this.tableName = tableName;
    }

    @Override
    protected String getSQL() {
	return "SELECT id, timestamp, data FROM " + tableName + " WHERE id = ?";
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, id);
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// TODO Auto-generated method stub

    }

    public static DBGenRecord execute(long id, BaseJdbcDAO dao, String tableName) throws OperationException,
	    DataConstraintException {
	GetGenericRecordSQL sql = new GetGenericRecordSQL(id, dao, tableName);
	sql.execute();
	return sql.record;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	if (rs.next()) {
	    record = new DBGenRecord();
	    record.setId(rs.getLong("id"));
	    record.setTimestamp(rs.getLong("timestamp"));
	    RecordSerialization.parseRecordData(rs.getBytes("data"), record);
	}
    }

}
