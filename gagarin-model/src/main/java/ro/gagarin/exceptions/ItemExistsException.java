package ro.gagarin.exceptions;

public class ItemExistsException extends DataConstraintException {

    private static final long serialVersionUID = 5953812143091133972L;

    private final String message;
    private final String fieldName;
    private final String className;

    public ItemExistsException(Class<?> itemClass, String fieldname, Exception e) {
	super(ErrorCodes.ITEM_EXISTS, e);
	this.message = "Field " + fieldname + " of type " + itemClass.getSimpleName() + " already exists";
	this.fieldName = fieldname;
	this.className = itemClass.getSimpleName();
    }

    @Override
    public String getMessage() {
	return this.message;
    }

    public String getFieldName() {
	return fieldName;
    }

    public String getClassName() {
	return className;
    }
}
