package ro.gagarin.exceptions;

public class ItemExistsException extends DataConstraintException {

	private static final long serialVersionUID = 5953812143091133972L;

	private String message;

	public ItemExistsException(Class<?> itemClass, String detail, Exception e) {
		super(ErrorCodes.ITEM_EXISTS, e);
		this.message = "Field " + detail + " of type " + itemClass.getSimpleName()
				+ " already exists";
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
