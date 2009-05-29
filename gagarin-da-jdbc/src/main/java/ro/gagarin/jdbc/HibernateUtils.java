package ro.gagarin.jdbc;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.FieldRequiredException;

public class HibernateUtils {
	private static final transient Logger LOG = Logger.getLogger(HibernateUtils.class);

	public static void requireStringField(String fieldname, Object object)
			throws FieldRequiredException {
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
			LOG.error("Error getting field " + fieldname + " of object "
					+ object.getClass().getSimpleName() + ": " + object);
			missingValue = true;
		}
		if (missingValue)
			throw new FieldRequiredException(fieldname, object.getClass());
	}
}
