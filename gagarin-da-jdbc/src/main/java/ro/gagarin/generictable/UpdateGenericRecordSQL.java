package ro.gagarin.generictable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.genericrecord.GenericRecordField;
import ro.gagarin.jdbc.UpdateQuery;

public class UpdateGenericRecordSQL extends UpdateQuery {

    private final GenericRecord record;
    private final String tableName;
    private String[] clearColumns;

    public UpdateGenericRecordSQL(GenericRecord record, JdbcGenericTableDAO dao, String tableName) {
	super(dao);
	this.record = record;
	this.tableName = tableName;
	this.clearColumns = dao.getMappingsForTable(tableName);
    }

    @Override
    protected String getSQL() {
	StringBuilder sb = new StringBuilder();
	sb.append("UPDATE ");
	sb.append(tableName);
	sb.append(" SET ");
	sb.append("data = ?, timestamp = ?");
	for (String column : clearColumns) {
	    sb.append(", ");
	    sb.append(column);
	    sb.append(" = ?");
	}
	sb.append(" WHERE ");
	sb.append("id = ? and timestamp = ?");
	return sb.toString();
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setBytes(1, RecordSerialization.serializeRecord(this.record));
	// TODO:(3) timestamp generation should happen on DB level
	stmnt.setLong(2, System.currentTimeMillis());
	int index = 3;
	for (String column : clearColumns) {
	    GenericRecordField field = record.getField(column);
	    String value = null;
	    if (field != null) {
		value = field.getFieldValue();
	    }
	    stmnt.setString(index++, value);
	}
	stmnt.setLong(index++, record.getId());
	stmnt.setLong(index++, record.getTimestamp());
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// TODO Auto-generated method stub
    }
}
