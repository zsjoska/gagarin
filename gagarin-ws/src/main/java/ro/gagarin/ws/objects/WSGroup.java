package ro.gagarin.ws.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.PersonTypesEnum;
import ro.gagarin.user.Group;
import ro.gagarin.utils.ConversionUtils;

public class WSGroup extends BaseEntity implements Group, ControlEntity {

    private String description;
    private String name;

    public WSGroup() {
    }

    public WSGroup(Group group) {
	this();
	super.setId(group.getId());
	this.name = group.getName();
	this.setDescription(group.getDescription());
    }

    @Override
    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public String toString() {
	return ConversionUtils.group2String(this);
    }

    @Override
    public PersonTypesEnum getType() {
	return PersonTypesEnum.GROUP;
    }

    @Override
    public String getName() {
	return this.name;
    }

    @Override
    public ControlEntityCategory getCat() {
	return ControlEntityCategory.GROUP;
    }

    public void setName(String name) {
	this.name = name;
    }
}
