package ro.gagarin.generictable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.genericrecord.GenericRecordField;

public class RecordSerialization {

    public static byte[] serializeRecord(GenericRecord record) {
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(os);
	try {
	    for (GenericRecordField field : record) {
		dos.writeUTF(field.getFieldName());
		dos.writeUTF(field.getFieldValue());
		dos.writeLong(field.getUpdateTimestamp());
	    }
	    dos.flush();
	} catch (IOException e) {
	}
	return os.toByteArray();
    }

    public static void parseRecordData(byte[] bytes, DBGenRecord record) {
	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
	try {
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
