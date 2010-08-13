package ro.gagarin.user;

public enum PermissionEnum {
    /**
     * View a specific object. Usually stronger than {@link #LIST}
     */
    VIEW,

    /**
     * Create an object. If this could refer to a predefined object like ADMIN
     * and controls of creation an arbitrary object.
     */
    CREATE,

    /**
     * Update an object.
     */
    UPDATE,

    /**
     * List of different objects.
     */
    LIST,

    /**
     * Delete a specific object.
     */
    DELETE,

    /**
     * Perform administration tasks on a specific object.
     */
    ADMIN,

    /**
     * Perform queries and reports on a specific object.
     */
    AUDIT,

    /**
     * Permission to be used when performing secondary login operation. This
     * permission could control if a user could select an object in the
     * application and thus login in that specific area of the application.
     */
    SELECT;

    public String toString() {
	return this.name();
    };

}
