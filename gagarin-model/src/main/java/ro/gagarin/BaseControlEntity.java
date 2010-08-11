package ro.gagarin;

import java.util.HashSet;

import ro.gagarin.user.UserRole;

/**
 * Base class for entities for which the access could be controlled by the
 * permission framework.<br>
 * The permission framework could assign a {@link UserRole} to this object for a
 * {@link Owner}
 * 
 * @author ZsJoska
 * 
 */
public class BaseControlEntity extends BaseEntity implements ControlEntity {

    private static HashSet<ControlEntityCategory> ceHash = new HashSet<ControlEntityCategory>();

    private final ControlEntityCategory cat;

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

    public ControlEntityCategory getCategory() {
	return cat;
    }
}
