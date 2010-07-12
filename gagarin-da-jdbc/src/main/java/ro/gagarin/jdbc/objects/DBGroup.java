package ro.gagarin.jdbc.objects;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.OwnerTypesEnum;
import ro.gagarin.user.Group;
import ro.gagarin.utils.ConversionUtils;

public class DBGroup extends BaseControlEntity implements Group {

    private String description;

    public DBGroup(Group group) {
	this();
	this.setId(group.getId());
	this.setName(group.getName());
	this.setDescription(group.getDescription());
    }

    public DBGroup() {
	super(ControlEntityCategory.GROUP);
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
    public OwnerTypesEnum getType() {
	return OwnerTypesEnum.GROUP;
    }

    @Override
    public String getTitle() {
	return this.getName();
    }
}
