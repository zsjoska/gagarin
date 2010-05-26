package ro.gagarin;

/**
 * Enumeration defining all control entities defined in the application.<br>
 * A control entity category will define the name of the DB table where the
 * entities are stored. The table must contain an <code>id</code> and
 * <code>name</code> field.
 * 
 * @author ZsJoska
 * 
 */
public enum ControlEntityCategory {

    /**
     * Admin Control Entity<br>
     * This control entity is a special one; it has no table associated to it.
     * Has only one instance.
     */
    ADMIN(null),

    /**
     * ControlEntity category for groups.
     */
    GROUP("Groups");

    private final String table;

    private ControlEntityCategory(String table) {
	this.table = table;
    }

    public String table() {
	return table;
    }
}
