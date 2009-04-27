package ro.gagarin.exceptions;

import org.apache.log4j.Logger;

public class FieldRequiredException extends ExceptionBase {

	private static final transient Logger LOG = Logger.getLogger(UserNotFoundException.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -2594027123632142087L;
	private final String fieldname;

	public FieldRequiredException(String fieldname, Class<?> pojoClass) {
		super(ErrorCodes.FIELD_REQUIRED);
		this.fieldname = fieldname;
		LOG.error("Field " + fieldname + " is required for " + pojoClass.getName());
	}

	public String getFieldname() {
		return fieldname;
	}

}
