package ro.gagarin.genericrecord;

import java.util.ArrayList;
import java.util.Iterator;

import ro.gagarin.BaseEntity;

public class AppGenRecord extends BaseEntity implements GenericRecord {

    private ArrayList<GenericRecordField> list = new ArrayList<GenericRecordField>();
    private long timestamp;

    @Override
    public Iterator<GenericRecordField> iterator() {
	return list.iterator();
    }

    @Override
    public void addField(GenericRecordField field) {
	this.list.add(field);
    }

    @Override
    public GenericRecordField getField(String fieldName) {
	for (GenericRecordField field : this.list) {
	    if (fieldName.equalsIgnoreCase(field.getFieldName())) {
		return field;
	    }
	}
	return null;
    }

    @Override
    public void replaceField(GenericRecordField field) {
	throw new RuntimeException("not implemented");
    }

    @Override
    public Long getTimestamp() {
	return this.timestamp;
    }
}
