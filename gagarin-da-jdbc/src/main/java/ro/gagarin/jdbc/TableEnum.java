package ro.gagarin.jdbc;

public enum TableEnum {
	/**
	 * 
	 */
	USERS(
			"SELECT * FROM Users",
			"CREATE TABLE Users (id bigint, name varchar(100), userName varchar(50), password varchar(50), roleid bigint)"),

	/**
	 * 
	 */
	USERROLES("SELECT * FROM UserRoles", "CREATE TABLE UserRoles (id bigint, roleName varchar(50))"),

	/**
	 * 
	 */
	USER_PERMISSIONS("SELECT * FROM UserPermissions",
			"CREATE TABLE UserPermissions (id bigint, permissionName varchar(50))"),

	/**
	 * 
	 */
	PERMISSION_ASSIGNMENT("SELECT * FROM PermissionAssignment",
			"CREATE TABLE PermissionAssignment (role_id bigint, perm_id bigint)");

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
