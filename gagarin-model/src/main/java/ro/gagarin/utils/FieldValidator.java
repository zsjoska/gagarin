package ro.gagarin.utils;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.FieldRequiredException;

public class FieldValidator {
    private static final transient Logger LOG = Logger.getLogger(FieldValidator.class);

    public static abstract class FieldChecker {
	private final String fieldName;
	private final Object object;

	public abstract Object validate();

	public FieldChecker(String fieldName, Object object) {
	    this.fieldName = fieldName;
	    this.object = object;
	}

	public void check() {

	    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	    String setMethodName = "set" + getMethodName.substring(3);
	    System.out.println(setMethodName);
	    Method getMethod;
	    try {
		getMethod = this.object.getClass().getMethod(getMethodName);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    Object validate = validate();
	    if (validate != null) {

	    }
	}

    }

    public static void requireLongField(String fieldname, Object object) {
	new FieldChecker(fieldname, object) {

	    @Override
	    public Object validate() {
		return null;
	    }
	}.check();
    }

    public static void requireStringField(String fieldname, Object object) throws FieldRequiredException {
	Object value;
	boolean missingValue = false;
	try {
	    // invoke the getter through reflection
	    // Method[] methods = object.getClass().getMethods();
	    value = object.getClass().getMethod(fieldname).invoke(object);
	    if (value instanceof String) {
		missingValue = ((String) value).length() == 0;

	    } else if (value == null) {
		missingValue = true;
	    }
	} catch (Exception e) {
	    LOG.error("Error getting field " + fieldname + " of object " + object.getClass().getSimpleName() + ": "
		    + object);
	    missingValue = true;
	}
	if (missingValue)
	    throw new FieldRequiredException(fieldname, object.getClass());
    }
}
