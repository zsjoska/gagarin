package ro.gagarin.exceptions;

public enum ErrorCodes {

    SESSION_NOT_FOUND, ITEM_NOT_FOUND, FIELD_REQUIRED, ITEM_EXISTS, ERROR_READING_FILE, FILE_SYNTAX_ERROR, STARTUP_FAILED, SQL_ERROR, DB_OP_ERROR, ERROR_WILD_SESSION, CONFIG_ENTRY_INVALID, LOGIN_REQUIRED, PERMISSION_DENIED,

    /**
     * Internal inconsistency was detected.<br>
     * e.g. we couldn't find an object but the search criteria was not directly
     * originated from the user.
     */
    INTERNAL_ERROR,

}
