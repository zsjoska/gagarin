package ro.gagarin.exceptions;

public class ItemNotFoundException extends ExceptionBase {

	private static final long serialVersionUID = 5953812143091133972L;

	private String message;

	public ItemNotFoundException(Class<?> itemClass, String detail) {
		super(ErrorCodes.ITEM_NOT_FOUND);
		this.message = "Item " + detail + " of type " + itemClass.getSimpleName() + "was not found";
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
