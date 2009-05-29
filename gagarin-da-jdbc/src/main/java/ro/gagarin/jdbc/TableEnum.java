package ro.gagarin.jdbc;

public enum TableEnum {
	/**
	 * 
	 */
	USERS(
			"SELECT * FROM Users",
			"CREATE TABLE Users (id bigint, name char(50), userName char(50), password char(50), roleid bigint)"),

	/**
	 * 
	 */
	USERROLES("SELECT * FROM UserRoles", "CREATE TABLE UserRoles (id bigint, roleName char(50))"),

	/**
	 * 
	 */
	USER_ERMISSIONS("SELECT * FROM UserPermissions",
			"CREATE TABLE UserPermissions (id bigint, permissionName char(50))");

	private final String test;
	private final String create;

	private TableEnum(String test, String create) {
		this.test = test;
		this.create = create;
	}

	public String getTest() {
		return test;
	}

	public String getCreate() {
		return create;
	}
}
