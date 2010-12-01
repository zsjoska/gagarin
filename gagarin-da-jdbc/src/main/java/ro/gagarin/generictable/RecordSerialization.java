package ro.gagarin.generictable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ro.gagarin.dao.BaseDAO;
import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.genericrecord.GenericRecordField;

public class RecordSerialization {

    private static final short CURRENT_RECORD_VERSION = 0;

    public static byte[] serializeRecord(GenericRecord record) {
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(os);
	try {
	    dos.writeShort(CURRENT_RECORD_VERSION);
	    for (GenericRecordField field : record) {
		if (field.getFieldName() != null && field.getFieldValue() != null && field.getUpdateTimestamp() != null) {
		    dos.writeUTF(field.getFieldName());
		    dos.writeUTF(field.getFieldValue());
		    dos.writeLong(field.getUpdateTimestamp());
		}
	    }
	    dos.flush();
	} catch (IOException e) {
	}
	// TODO:(5) verify data size
	return os.toByteArray();
    }

    public static void parseRecordData(BaseDAO dao, byte[] bytes, DBGenRecord record) {
	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
	try {
	    short recordVersion = dis.readShort();
	    if (recordVersion != CURRENT_RECORD_VERSION) {
		// older read should be invoked
		dao.getLogger().error(
			"Mismatching record version: Actual: " + CURRENT_RECORD_VERSION + " read:" + recordVersion);
		return;
	    }
	    while (true) {

		DBGenRecordField field = new DBGenRecordField();
		field.setFieldName(dis.readUTF());
		field.setFieldValue(dis.readUTF());
		field.setUpdateTimestamp(dis.readLong());
		record.addField(field);
	    }
	} catch (IOException e) {
	    // end of values
	}
    }
}
