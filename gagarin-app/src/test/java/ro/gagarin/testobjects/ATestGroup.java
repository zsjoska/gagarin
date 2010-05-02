package ro.gagarin.testobjects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.Group;

public class ATestGroup extends BaseEntity implements Group {

    private String name;
    private String description;

    public ATestGroup(Group group) {
	this.setId(group.getId());
	this.setName(group.getName());
	this.setDescription(group.getDescription());
    }

    public ATestGroup() {
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
	return "DBGroup [description=" + description + ", name=" + name + ", getId()=" + getId() + "]";
    }

}
