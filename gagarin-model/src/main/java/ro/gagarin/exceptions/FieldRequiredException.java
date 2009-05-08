package ro.gagarin.exceptions;

import org.apache.log4j.Logger;

public class FieldRequiredException extends ExceptionBase {

	private static final transient Logger LOG = Logger.getLogger(ItemNotFoundException.class);

	private static final long serialVersionUID = -2594027123632142087L;
	private final String message;

	public FieldRequiredException(String fieldname, Class<?> pojoClass) {
		super(ErrorCodes.FIELD_REQUIRED);
		this.message = "Field " + fieldname + " is required for " + pojoClass.getName();
		LOG.error(message);
	}

	@Override
	public String getMessage() {
		return message;
	}

}
