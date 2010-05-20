package ro.gagarin;

import java.util.HashSet;

public class ControlEntity extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -4483417459434469304L;
    private static final Long ADMIN_CONTROL_ENTITY_ID = 1L;

    private static HashSet<ControlEntityCategory> ceHash = new HashSet<ControlEntityCategory>();

    private static final ControlEntity ADMIN_ENTITY;

    static {
	ADMIN_ENTITY = new ControlEntity(ControlEntityCategory.ADMIN);
	ADMIN_ENTITY.setId(ADMIN_CONTROL_ENTITY_ID);
	ADMIN_ENTITY.setName("ADMIN");
    }

    public ControlEntity(ControlEntityCategory cat) {
	ceHash.add(cat);
    }

    public Class<?> getControlEntityClass() {
	return ControlEntity.class;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    private String name;

    public static ControlEntity getAdminEntity() {
	return ADMIN_ENTITY;
    }
}
