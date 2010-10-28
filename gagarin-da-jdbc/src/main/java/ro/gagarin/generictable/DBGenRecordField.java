package ro.gagarin.generictable;

import ro.gagarin.genericrecord.GenericRecordField;

public class DBGenRecordField implements GenericRecordField {
    private String fieldName;
    private String fieldValue;
    private Long updateTimestamp;

    @Override
    public String getFieldName() {
	return this.fieldName;
    }

    @Override
    public Long getUpdateTimestamp() {
	return updateTimestamp;
    }

    @Override
    public String getFieldValue() {
	return fieldValue;
    }

    public DBGenRecordField setFieldName(String fieldName) {
	this.fieldName = fieldName;
	return this;
    }

    public DBGenRecordField setFieldValue(String fieldValue) {
	this.fieldValue = fieldValue;
	return this;
    }

    public DBGenRecordField setUpdateTimestamp(Long updateTimestamp) {
	this.updateTimestamp = updateTimestamp;
	return this;
    }

}
