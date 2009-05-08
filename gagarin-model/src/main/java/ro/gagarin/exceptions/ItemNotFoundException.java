package ro.gagarin.exceptions;

import org.apache.log4j.Logger;

public class ItemNotFoundException extends ExceptionBase {

	private static final transient Logger LOG = Logger.getLogger(ItemNotFoundException.class);

	private String message;

	public ItemNotFoundException() {
		super(ErrorCodes.ITEM_NOT_FOUND);
	}

	public ItemNotFoundException(Class<?> itemClass, String detail) {
		super(ErrorCodes.ITEM_NOT_FOUND);
		this.message = "Item " + detail + " of type " + itemClass.getSimpleName() + "was not found";
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
