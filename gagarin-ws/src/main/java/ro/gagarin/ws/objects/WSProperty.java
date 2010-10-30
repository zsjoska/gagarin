package ro.gagarin.ws.objects;

import ro.gagarin.genericrecord.GenericRecordField;

public class WSProperty implements GenericRecordField {

    private String fieldName;
    private String fieldValue;
    private Long updateTimestamp = System.currentTimeMillis();

    public WSProperty() {
    }

    public static WSProperty createFromGenericRecordField(GenericRecordField field) {
	WSProperty wsProperty = new WSProperty();
	wsProperty.fieldName = field.getFieldName();
	wsProperty.fieldValue = field.getFieldValue();
	wsProperty.updateTimestamp = field.getUpdateTimestamp();
	return wsProperty;
    }

    public String getFieldName() {
	return this.fieldName;
    }

    public Long getUpdateTimestamp() {
	return this.updateTimestamp;
    }

    public String getFieldValue() {
	return this.fieldValue;
    }

    public WSProperty setFieldName(String fieldName) {
	this.fieldName = fieldName;
	return this;
    }

    public WSProperty setFieldValue(String fieldValue) {
	this.fieldValue = fieldValue;
	return this;
    }

    public WSProperty setUpdateTimestamp(Long updateTimestamp) {
	this.updateTimestamp = updateTimestamp;
	return this;
    }

}
