package ro.gagarin;

public abstract class ControlEntity extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -4483417459434469304L;

    public Class<?> getControlEntityClass() {
	return ControlEntity.class;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getDisplayName() {
	return displayName;
    }

    private String displayName;

}
