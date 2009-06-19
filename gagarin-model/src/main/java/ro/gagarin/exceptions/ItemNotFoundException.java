package ro.gagarin.exceptions;

public class ItemNotFoundException extends ExceptionBase {

	private static final long serialVersionUID = 5953812143091133972L;

	private String message;

	private final String className;

	public ItemNotFoundException(Class<?> itemClass, String detail) {
		super(ErrorCodes.ITEM_NOT_FOUND, detail);
		this.className = itemClass.getSimpleName();
		this.message = "Item " + detail + " of type " + itemClass.getSimpleName() + "was not found";
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public String getClassName() {
		return className;
	}
}
