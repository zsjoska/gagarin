package ro.gagarin;

import java.util.HashSet;

import ro.gagarin.user.UserRole;

/**
 * Base class for entities for which the access could be controlled by the
 * permission framework.<br>
 * The permission framework could assign a {@link UserRole} to this object for a
 * {@link Person}
 * 
 * @author ZsJoska
 * 
 */
public class BaseControlEntity extends BaseEntity implements ControlEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -4483417459434469304L;
    private static final Long ADMIN_CONTROL_ENTITY_ID = 1L;

    private static HashSet<ControlEntityCategory> ceHash = new HashSet<ControlEntityCategory>();

    private static final BaseControlEntity ADMIN_ENTITY;
    private final ControlEntityCategory cat;

    static {
	ADMIN_ENTITY = new BaseControlEntity(ControlEntityCategory.ADMIN);
	ADMIN_ENTITY.setId(ADMIN_CONTROL_ENTITY_ID);
	ADMIN_ENTITY.setName("ADMIN");
    }

    public BaseControlEntity(ControlEntityCategory cat) {
	this.cat = cat;
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

    /**
     * A special control entity is returned which is used for Adminitrator
     * operations.
     * 
     * @return the admin control entity
     */
    public static ControlEntity getAdminEntity() {
	return ADMIN_ENTITY;
    }

    @Override
    public int hashCode() {
	return this.getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof ControlEntity) {
	    ControlEntity ce = (ControlEntity) obj;
	    return ce.getId().equals(this.getId());
	}
	return false;
    }

    public ControlEntityCategory getCat() {
	return cat;
    }
}
