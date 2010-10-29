package ro.gagarin.genericrecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ro.gagarin.BaseEntity;
import ro.gagarin.utils.ConversionUtils;

public class AppGenRecord extends BaseEntity implements GenericRecord {

    private ArrayList<GenericRecordField> list = new ArrayList<GenericRecordField>();
    private long timestamp;

    public AppGenRecord(long recordId) {
	this.setId(recordId);
    }

    public AppGenRecord() {
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
