package ro.gagarin.genericrecord;

import ro.gagarin.Entity;

public interface GenericRecord extends Iterable<GenericRecordField>, Entity {

    Long getTimestamp();

    void addField(GenericRecordField field);

    GenericRecordField getField(String fieldName);

    void replaceField(GenericRecordField field);
}
