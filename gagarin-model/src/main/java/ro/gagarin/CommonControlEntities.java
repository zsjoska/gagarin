package ro.gagarin;

public enum CommonControlEntities implements ControlEntity {

    /**
     * 
     */
    ADMIN_CE(ControlEntityCategory.ADMIN, 1, "ADMIN"),

    /**
     * 
     */
    USER_CE(ControlEntityCategory.ADMIN, 2, "USER"),

    /**
     * 
     */
    GROUP_CE(ControlEntityCategory.ADMIN, 4, "GROUP"),

    /**
     * 
     */
    PERMISSION_CE(ControlEntityCategory.ADMIN, 5, "PERMISSION");

    private final ControlEntityCategory category;
    private final long id;
    private final String name;

    private CommonControlEntities(ControlEntityCategory category, long id, String name) {
	this.category = category;
	this.id = id;
	this.name = name;

    }

    @Override
    public Long getId() {
	return this.id;
    }

    @Override
    public String getName() {
	return this.name;
    }

    @Override
    public ControlEntityCategory getCategory() {
	return this.category;
    }
}
