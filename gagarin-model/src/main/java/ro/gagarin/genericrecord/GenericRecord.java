package ro.gagarin.genericrecord;

import java.util.List;

import ro.gagarin.Entity;

public interface GenericRecord extends Iterable<GenericRecordField>, Entity {

    Long getTimestamp();

    GenericRecordField getField(String fieldName);

    List<GenericRecordField> getFieldList();
}
