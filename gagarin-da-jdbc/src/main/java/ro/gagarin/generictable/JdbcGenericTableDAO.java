package ro.gagarin.generictable;

import java.util.HashMap;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.genericrecord.GenericRecordField;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.session.Session;

public class JdbcGenericTableDAO extends BaseJdbcDAO {

    private static final HashMap<String, String[]> GENERIC_TABLE_MAPPINGS = new HashMap<String, String[]>();

    static {
	GENERIC_TABLE_MAPPINGS.put("UsersExtra", new String[] { "name" });
    }

    private final String tableName;

    public JdbcGenericTableDAO(Session session, String tableName) throws OperationException {
	super(session);
	this.tableName = tableName;
    }

    public void updateRecord(GenericRecord record) throws OperationException, DataConstraintException {
	// TODO:(5) This could go to cache
	int result = 0;

	if (record.getTimestamp() != null) {
	    // if we have a timestamp, we consider originating from the DB and
	    // ready merged
	    result = new UpdateGenericRecordSQL(record, this, tableName).execute();
	    if (result != 0) {
		return;
	    }
	}

	// otherwise, the record has a problem: inexistent or modified meanwhile
	DBGenRecord reference = GetGenericRecordSQL.execute(record.getId(), this, tableName);
	if (reference == null) {
	    try {
		result = new InsertGenericRecordSQL(record, this, tableName).execute();
		return;
	    } catch (DataConstraintException e) {
		reference = GetGenericRecordSQL.execute(record.getId(), this, tableName);
	    }
	}
	while (result == 0) {

	    // TODO:(2) We have to implement a delay here. One guess would be to
	    // implement a delay with random and generate the random based on
	    // the iteration count

	    GenericRecord merge = mergeRecord(reference, record);
	    result = new UpdateGenericRecordSQL(merge, this, tableName).execute();
	    if (result == 0) {
		reference = GetGenericRecordSQL.execute(record.getId(), this, tableName);
	    }
	}
    }

    private DBGenRecord mergeRecord(DBGenRecord reference, GenericRecord record) {
	for (GenericRecordField field : record) {

	    // TODO:(1) Class cast exception was seen here; the class originated
	    // from WS
	    // safe to cast since the reference always comes from DB
	    DBGenRecordField refField = (DBGenRecordField) reference.getField(field.getFieldName());

	    if (refField == null) {
		reference.addField(field);
	    } else {
		if (refField.getUpdateTimestamp() < field.getUpdateTimestamp()) {

		    refField.setFieldValue(field.getFieldValue());
		    refField.setUpdateTimestamp(field.getUpdateTimestamp());
		} // else... mid-air collision... decide later
	    }
	}
	return reference;
    }

    public GenericRecord getRecord(long id) throws OperationException {
	return GetGenericRecordSQL.execute(id, this, tableName);
    }

    public void deleteRecord(Long id) throws OperationException, DataConstraintException {
	new DeleteGenericRecordSQL(id, this, tableName).execute();
    }

    public String[] getMappingsForTable(String tableName) {
	String[] mappings = GENERIC_TABLE_MAPPINGS.get(tableName);
	if (mappings == null) {
	    APPLOG.warn("Mapping definition was not found for " + tableName);
	    mappings = new String[0];
	}
	return mappings;
    }

}