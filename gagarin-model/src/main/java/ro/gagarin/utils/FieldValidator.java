package ro.gagarin.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.FieldRequiredException;

public class FieldValidator {
    private static final transient Logger LOG = Logger.getLogger(FieldValidator.class);

    public static abstract class FieldChecker {
	private final String fieldName;
	private final Object object;
	private final Class<?> dstClass;

	public abstract Object validate(Object object) throws Exception;

	public FieldChecker(String fieldName, Object object, Class<?> dstClass) {
	    this.fieldName = fieldName;
	    this.object = object;
	    this.dstClass = dstClass;
	}

	public void check() throws FieldRequiredException {

	    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	    String setMethodName = "set" + getMethodName.substring(3);
	    Method getMethod;
	    try {
		getMethod = this.object.getClass().getMethod(getMethodName);
	    } catch (Exception e) {
		LOG.error("Error getting " + getMethodName + " method", e);
		throw new RuntimeException(e);
	    }
	    Object validate;
	    try {
		Object value = getMethod.invoke(this.object);
		if (value == null)
		    throw new FieldRequiredException(fieldName, object.getClass());

		if (!this.dstClass.isInstance(value)) {
		    throw new RuntimeException("Wrong type: Expected: " + this.dstClass.getName() + " Received: "
			    + value.getClass().getName());
		}
		validate = validate(value);
		if (validate != null) {
		    Method setMethod = null;
		    try {
			setMethod = this.object.getClass().getMethod(setMethodName, this.dstClass);
		    } catch (Exception e) {
			LOG.error("Error getting " + setMethodName + " method", e);
			throw new RuntimeException(e);
		    }
		    setMethod.invoke(this.object, validate);
		}
	    } catch (RuntimeException e) {
		throw e;
	    } catch (FieldRequiredException e) {
		throw e;
	    } catch (Exception e) {
		LOG.error("Error checking the field with method " + getMethodName + " method", e);
		throw new RuntimeException(e);
	    }
	}
    }

    public static void requireLongField(final String fieldname, final Object object) throws FieldRequiredException {
	new FieldChecker(fieldname, object, Long.class) {

	    @Override
	    public Object validate(Object value) throws Exception {
		return null;
	    }
	}.check();
    }

    public static void requireField(final String fieldname, final Object object) throws FieldRequiredException {
	new FieldChecker(fieldname, object, Object.class) {
	    @Override
	    public Object validate(Object value) throws Exception {
		return null;
	    }
	}.check();
    }

    public static void requireStringField(final String fieldname, final Object object, final boolean trim)
	    throws FieldRequiredException {
	new FieldChecker(fieldname, object, String.class) {

	    @Override
	    public Object validate(Object value) throws Exception {
		String str = (String) value;

		if (!trim) {
		    if (str.length() == 0)
			throw new FieldRequiredException(fieldname, object.getClass());
		    return null;
		}

		str = str.trim();
		if (str.length() == 0)
		    throw new FieldRequiredException(fieldname, object.getClass());

		if (str.length() != ((String) value).length()) {
		    return str;
		}
		return null;
	    }
	}.check();
    }

    public static String checkStringValue(String str, String name, int maxLength) throws FieldRequiredException {

	// TODO: fix this Object.class, maybe with a permisible constructor
	if (str == null)
	    throw new FieldRequiredException(name, Object.class);
	String newStr = str.trim();

	if (newStr.length() == 0)
	    throw new FieldRequiredException(name, Object.class);

	if (newStr.length() > maxLength)
	    newStr = newStr.substring(0, maxLength);

	return newStr;
    }

    public static void checkAllFields(Object object) throws FieldRequiredException {
	Field[] fields = object.getClass().getDeclaredFields();
	for (Field field : fields) {
	    Class<?> declaringClass = field.getType();
	    if (String.class.equals(declaringClass)) {
		requireStringField(field.getName(), object, false);
	    } else if (Long.class.equals(declaringClass)) {
		requireLongField(field.getName(), object);
	    }
	}
    }
}
