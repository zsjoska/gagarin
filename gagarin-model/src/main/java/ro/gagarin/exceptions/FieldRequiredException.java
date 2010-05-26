package ro.gagarin.exceptions;

import org.apache.log4j.Logger;

public class FieldRequiredException extends DataConstraintException {

    private static final transient Logger LOG = Logger.getLogger(ItemNotFoundException.class);

    private static final long serialVersionUID = -2594027123632142087L;
    private final String message;

    private final String fieldName;
    private final String className;

    public FieldRequiredException(String fieldname, Class<?> pojoClass) {
	super(ErrorCodes.FIELD_REQUIRED);
	this.message = "Field " + fieldname + " is required for " + pojoClass.getName();
	this.fieldName = fieldname;
	this.className = pojoClass.getSimpleName();
	LOG.error(message);
    }

    public FieldRequiredException(String fieldname, String className) {
	super(ErrorCodes.FIELD_REQUIRED);
	this.message = "Field " + fieldname + " is required for " + className;
	this.fieldName = fieldname;
	this.className = className;
	LOG.error(message);
    }

    public FieldRequiredException(String fieldname) {
	super(ErrorCodes.FIELD_REQUIRED);
	this.message = "Field " + fieldname + " is required";
	this.fieldName = fieldname;
	this.className = "unknown";
	LOG.error(message);
    }

    public FieldRequiredException(String fieldname, Class<?> pojoClass, Exception e) {
	super(ErrorCodes.FIELD_REQUIRED, e);
	this.message = "Field " + fieldname + " is required for " + pojoClass.getName();
	this.fieldName = fieldname;
	this.className = pojoClass.getSimpleName();
	LOG.error(message);
    }

    @Override
    public String getMessage() {
	return message;
    }

    public String getFieldName() {
	return fieldName;
    }

    public String getClassName() {
	return className;
    }

}
