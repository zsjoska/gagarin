package ro.gagarin.application.objects;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.PersonTypesEnum;
import ro.gagarin.user.Group;
import ro.gagarin.utils.ConversionUtils;

public class AppGroup extends BaseControlEntity implements Group {

    private String description;

    public AppGroup() {
	super(ControlEntityCategory.GROUP);
    }

    public AppGroup(Group group) {
	this();
	this.setId(group.getId());
	this.setName(group.getName());
	this.setDescription(group.getDescription());
    }

    @Override
    public PersonTypesEnum getType() {
	return PersonTypesEnum.GROUP;
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
    public String getTitle() {
	return this.getName();
    }
}
