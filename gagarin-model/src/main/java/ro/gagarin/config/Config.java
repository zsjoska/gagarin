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

	JDBC_CONNECTION_URL(""), JDBC_DB_USER(""), JDBC_DB_PASSWORD("");

	private final String defValue;

	Config(String defValue) {
		this.defValue = defValue;
	}

	public String getDefaultValue() {
		return defValue;
	}
}
