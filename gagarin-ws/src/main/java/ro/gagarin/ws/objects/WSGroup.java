package ro.gagarin.ws.objects;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.PersonTypesEnum;
import ro.gagarin.user.Group;
import ro.gagarin.utils.ConversionUtils;

public class WSGroup extends BaseControlEntity implements Group {

    private String description;

    public WSGroup() {
	super(ControlEntityCategory.GROUP);
    }

    public WSGroup(Group group) {
	this();
	this.setId(group.getId());
	this.setName(group.getName());
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
}
