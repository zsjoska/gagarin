package ro.gagarin.exceptions;

public enum ErrorCodes {

    /**
     * The provided session identifier was not found or there is a request that
     * uses in this moment.
     */
    SESSION_NOT_FOUND,

    /**
     * The requested item was not found.
     */
    ITEM_NOT_FOUND,

    /**
     * A required filed was missing.
     */
    FIELD_REQUIRED,

    /**
     * The item is already exists
     */
    ITEM_EXISTS,

    /**
     * There was an error reading the file from the file system or classpath.
     */
    ERROR_READING_FILE,

    /**
     * The syntax or content of the file is invalid.
     */
    FILE_SYNTAX_ERROR,

    /**
     * The application initializer was unable to startup properly. Check the
     * configuration location or database settings. (these are the most common
     * reasons)
     */
    STARTUP_FAILED,

    /**
     * There is a syntax error in the SQL command or one of the provided
     * parameters was invalid.
     */
    SQL_ERROR,

    /**
     * Generic database operation error. Check the logs.
     */
    DB_OP_ERROR,

    /**
     * Session related inconsistency detected. The session was not marked busy.
     */
    ERROR_WILD_SESSION,

    /**
     * The requested operation needs requires log-in on the session.
     */
    LOGIN_REQUIRED,

    /**
     * The requested operation was denied due to insufficient privileges.
     */
    PERMISSION_DENIED,

    /**
     * Internal inconsistency was detected.<br>
     * e.g. we couldn't find an object but the search criteria was not directly
     * originated from the user.
     */
    INTERNAL_ERROR,

    /**
     * This feature is disabled by the current configuration.
     */
    FEATURE_DISABLED,

    /**
     * The referenced authenticator was not found.<br>
     * A reason could be that the use had this authenticator in the DB but was
     * removed from the application.
     */
    AUTHENTICATOR_NOT_FOUND,
}
