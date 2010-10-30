package ro.gagarin.generictable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ro.gagarin.BaseEntity;
import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.genericrecord.GenericRecordField;
import ro.gagarin.utils.ConversionUtils;

public class DBGenRecord extends BaseEntity implements GenericRecord {

    private ArrayList<GenericRecordField> list = new ArrayList<GenericRecordField>();
    private Long timestamp;

    public DBGenRecord() {
    }

    public DBGenRecord(GenericRecord record) {
	this.list = new ArrayList<GenericRecordField>(record.getFieldList());
	this.timestamp = record.getTimestamp();
	this.setId(record.getId());
    }

    @Override
    public Iterator<GenericRecordField> iterator() {
	return list.iterator();
    }

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

    public DBGenRecord setTimestamp(long timestamp) {
	this.timestamp = timestamp;
	return this;
    }

    @Override
    public Long getTimestamp() {
	return this.timestamp;
    }

    @Override
    public String toString() {
	return ConversionUtils.genericRecord2String(this);
    }

    @Override
    public List<GenericRecordField> getFieldList() {
	return this.list;
    }
}
