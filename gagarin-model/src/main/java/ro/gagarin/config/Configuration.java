package ro.gagarin.config;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;

public class Configuration {
    private static final transient Logger LOG = Logger.getLogger(Configuration.class);

    private static final HashMap<String, String> CUSTOM_ENTRIES = new HashMap<String, String>();

    public static volatile Long USER_SESSION_TIMEOUT = 0l;

    public static volatile String ADMIN_ROLE_NAME = "ADMIN_ROLE";

    public static volatile Long SESSION_CHECK_PERIOD = 30000l;

    /**
     * 
     */
    public static volatile String ADMIN_USER_NAME = "admin";

    /**
     * 
     */
    public static volatile String ADMIN_PASSWORD = "password";

    /**
     * 
     */
    public static volatile String JDBC_DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    /**
     * 
     */
    public static volatile String JDBC_CONNECTION_URL = "jdbc:derby:target/derbydb;create=true";
    public static volatile String JDBC_DB_USER = "";
    public static volatile String JDBC_DB_PASSWORD = "";

    /**
     * Location of the file containing the database initialization script
     */
    public static volatile String DB_INIT_SQL_FILE = "dbInit.sql";

    /**
     * The period of time for checking DB configuration changes
     */
    public static volatile Long DB_CONFIG_CHECK_PERIOD = 120000L;

    /**
     * Dummy configuration entry for storing the last modification time of the
     * configuration
     */
    public static volatile Long _LAST_UPDATE_TIME_ = System.currentTimeMillis();

    public static volatile Long FILE_CHECK_INTERVAL = 10000L;

    public static volatile String _TEST_DB_ONLY_ = "_TEST_DB_ONLY_";
    public static volatile String _TEST_LOCAL_ONLY_ = "_TEST_LOCAL_ONLY_";
    public static volatile String _TEST_DB_AND_FILE_ = "_TEST_DB_AND_FILE_";

    /**
     * Controls whether users are allowed to register
     */
    public static volatile Boolean ALLOW_USER_REGISTRATION = true;

    /**
     * Value in miliseconds, the time until the activation key for a
     * registration is valid.<br>
     * 172800000 = 2 days
     */
    public static volatile Long REGISTRATION_VALIDITY = (long) 1000 * 60 * 60 * 48;

    public static volatile String ADMIN_GROUP_NAME = "Admin Group";

    public static String getCustomConfig(String key) {
	return CUSTOM_ENTRIES.get(key);
    }

    private static void setCustomEntry(String key, String value) {
	CUSTOM_ENTRIES.put(key, value);
    }

    public static boolean setConfig(String key, String value) throws OperationException {
	if (key == null || value == null) {
	    throw new OperationException(ErrorCodes.INVALID_CONFIG_VALUE, key + "=" + value);
	}
	try {
	    Field field = Configuration.class.getField(key);
	    Object oldValue = field.get(null);
	    Object newValue = oldValue;
	    if (oldValue instanceof Long) {
		newValue = Long.valueOf(value);
	    } else if (oldValue instanceof String) {
		newValue = value;
	    } else if (oldValue instanceof Boolean) {
		newValue = Boolean.valueOf(value);
	    } else {
		throw new OperationException(ErrorCodes.INTERNAL_ERROR, "No conversion method defined for " + key + "="
			+ value);
	    }
	    if (oldValue.equals(newValue)) {
		return false;
	    }
	    field.set(null, newValue);
	    return true;
	} catch (NoSuchFieldException e) {
	    String customConfig = getCustomConfig(key);
	    if (value.equals(customConfig)) {
		return false;
	    }
	    setCustomEntry(key, value);
	    return true;
	} catch (NumberFormatException e) {
	    throw new OperationException(ErrorCodes.INVALID_CONFIG_VALUE, key + "=" + value);
	} catch (SecurityException e) {
	    throw new OperationException(ErrorCodes.INTERNAL_ERROR, e);
	} catch (IllegalArgumentException e) {
	    throw new OperationException(ErrorCodes.INTERNAL_ERROR, e);
	} catch (IllegalAccessException e) {
	    throw new OperationException(ErrorCodes.INTERNAL_ERROR, e);
	}
    }

    public static String getAsString(String key) {
	Field field;
	try {
	    field = Configuration.class.getField(key);
	    Object value = field.get(null);
	    return value.toString();
	} catch (NoSuchFieldException e) {
	    return getCustomConfig(key);
	} catch (Exception e) {
	    LOG.error("Could not get the value of" + key, e);
	}
	return null;
    }
}
