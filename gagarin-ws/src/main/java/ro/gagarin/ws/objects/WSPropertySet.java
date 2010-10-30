package ro.gagarin.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ro.gagarin.BaseEntity;
import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.genericrecord.GenericRecordField;
import ro.gagarin.utils.ConversionUtils;

public class WSPropertySet extends BaseEntity implements GenericRecord {

    private List<WSProperty> properties = new ArrayList<WSProperty>();

    private Long timestamp;

    public Long getTimestamp() {
	return this.timestamp;
    }

    public List<WSProperty> getFields() {
	return this.properties;
    }

    public WSPropertySet setFields(List<WSProperty> props) {
	this.properties = props;
	return this;
    }

    public WSPropertySet setTimestamp(Long timestamp) {
	this.timestamp = timestamp;
	return this;
    }

    @Override
    public Iterator<GenericRecordField> iterator() {
	return getFieldList().iterator();
    }

    @Override
    public GenericRecordField getField(String fieldName) {
	for (WSProperty property : this.properties) {
	    if (fieldName.equalsIgnoreCase(property.getFieldName())) {
		return property;
	    }
	}
	return null;
    }

    @Override
    public List<GenericRecordField> getFieldList() {
	ArrayList<GenericRecordField> fieldList = new ArrayList<GenericRecordField>();
	for (WSProperty property : this.properties) {
	    fieldList.add(property);
	}
	return fieldList;
    }

    @Override
    public String toString() {
	return ConversionUtils.genericRecord2String(this);
    }

    public WSPropertySet addField(WSProperty prop) {
	this.properties.add(prop);
	return this;
    }
}
