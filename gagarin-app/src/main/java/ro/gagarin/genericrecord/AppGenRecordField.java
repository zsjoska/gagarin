package ro.gagarin.genericrecord;

public class AppGenRecordField implements GenericRecordField {
    private String fieldName;
    private String fieldValue;
    private Long updateTimestamp = System.currentTimeMillis();

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

    public AppGenRecordField setFieldName(String fieldName) {
	this.fieldName = fieldName;
	return this;
    }

    public AppGenRecordField setFieldValue(String fieldValue) {
	this.fieldValue = fieldValue;
	return this;
    }

    public AppGenRecordField setUpdateTimestamp(Long updateTimestamp) {
	this.updateTimestamp = updateTimestamp;
	return this;
    }

}
