package ro.gagarin.exceptions;

import org.apache.log4j.Logger;

public class ItemExistsException extends ExceptionBase {

	private static final transient Logger LOG = Logger.getLogger(ItemExistsException.class);

	private String message;

	public ItemExistsException() {
		super(ErrorCodes.ITEM_EXISTS);
	}

	public ItemExistsException(Class<?> itemClass, String detail) {
		super(ErrorCodes.ITEM_NOT_FOUND);
		this.message = "Item " + detail + " of type " + itemClass.getSimpleName()
				+ " already exists";
		LOG.error(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5953812143091133972L;

	@Override
	public String getMessage() {
		return this.message;
	}
}
