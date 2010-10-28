package ro.gagarin.generictable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.genericrecord.GenericRecordField;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;

public class InsertGenericRecordSQL extends UpdateQuery {

    private final GenericRecord record;
    private final String tableName;
    private String[] clearColumns;

    public InsertGenericRecordSQL(GenericRecord record, BaseJdbcDAO dao, String tableName) {
	super(dao);
	this.record = record;
	this.tableName = tableName;
	this.clearColumns = new String[] { "name" };
    }

    @Override
    protected String getSQL() {
	StringBuilder sb = new StringBuilder();
	sb.append("INSERT INTO ");
	sb.append(tableName);
	sb.append("(");
	sb.append("id, data, timestamp");
	for (String column : clearColumns) {
	    sb.append(", ");
	    sb.append(column);
	}
	sb.append(") VALUES (");
	sb.append("?,?,?");
	for (int i = 0; i < clearColumns.length; i++) {
	    sb.append(",?");
	}
	sb.append(")");
	return sb.toString();
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, record.getId());
	stmnt.setBytes(2, RecordSerialization.serializeRecord(this.record));
	// TODO:(3) timestamp generation should happen on DB level
	stmnt.setLong(3, System.currentTimeMillis());
	int index = 4;
	for (String column : clearColumns) {
	    GenericRecordField field = record.getField(column);
	    String value = null;
	    if (field != null) {
		value = field.getFieldValue();
	    }
	    stmnt.setString(index++, value);
	}

    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// TODO Auto-generated method stub
    }
}
