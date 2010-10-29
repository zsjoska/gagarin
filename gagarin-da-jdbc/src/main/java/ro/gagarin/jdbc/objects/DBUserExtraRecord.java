package ro.gagarin.jdbc.objects;

import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.generictable.DBGenRecord;
import ro.gagarin.user.UserExtraRecord;

public class DBUserExtraRecord extends DBGenRecord implements UserExtraRecord {
    public DBUserExtraRecord() {
    }

    public DBUserExtraRecord(GenericRecord record) {
	super(record);
    }
}
