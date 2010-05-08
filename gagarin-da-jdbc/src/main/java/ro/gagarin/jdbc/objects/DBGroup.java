package ro.gagarin.jdbc.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.Group;
import ro.gagarin.utils.ConversionUtils;

public class DBGroup extends BaseEntity implements Group {

    private String name;
    private String description;

    public DBGroup(Group group) {
	this.setId(group.getId());
	this.setName(group.getName());
	this.setDescription(group.getDescription());
    }

    public DBGroup() {
    }

    @Override
    public String getDescription() {
	return this.description;
    }

    @Override
    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public String toString() {
	return ConversionUtils.group2String(this);
    }
}
