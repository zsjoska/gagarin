package ro.gagarin.config;

public enum Config {

    /**
     * 
     */
    USER_SESSION_TIMEOUT("" + 1000 * 60 * 5),

    /**
     * 
     */
    SESSION_CHECK_PERIOD("" + 1000 * 30),

    /**
     * 
     */
    ADMIN_ROLE_NAME("ADMIN_ROLE"),

    /**
     * 
     */
    ADMIN_USER_NAME("admin"),

    /**
     * 
     */
    ADMIN_PASSWORD("password"),

    /**
     * 
     */
    JDBC_DB_DRIVER("org.apache.derby.jdbc.EmbeddedDriver"),

    /**
     * 
     */
    JDBC_CONNECTION_URL("jdbc:derby:target/derbydb;create=true"), JDBC_DB_USER(""), JDBC_DB_PASSWORD(""),

    /**
     * Location of the file containing the database initialization script
     */
    DB_INIT_SQL_FILE("dbInit.sql"),

    /**
     * The period of time for checking DB configuration changes
     */
    DB_CONFIG_CHECK_PERIOD("120000"),

    /**
     * Dummy configuration entry for storing the last modification time of the
     * configuration
     */
    _LAST_UPDATE_TIME_(null),

    _TEST_DB_ONLY_("_TEST_DB_ONLY_"),

    _TEST_LOCAL_ONLY_("_TEST_LOCAL_ONLY_"), FILE_CHECK_INTERVAL("10000");

    private final String defValue;

    Config(String defValue) {
	this.defValue = defValue;
    }

    public String getDefaultValue() {
	return defValue;
    }
}
