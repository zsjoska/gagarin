package ro.gagarin;

public abstract class ControlEntity extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -4483417459434469304L;

    public Class<?> getControlEntityClass() {
	return ControlEntity.class;
    }

    private String displayName;

}
