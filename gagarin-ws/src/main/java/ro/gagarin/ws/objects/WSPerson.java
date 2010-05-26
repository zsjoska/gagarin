package ro.gagarin.ws.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.Person;
import ro.gagarin.PersonTypesEnum;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.utils.ConversionUtils;

public class WSPerson extends BaseEntity implements Person {

    private PersonTypesEnum type;
    private String title;

    public WSPerson() {

    }

    public WSPerson(Group group) {
	this.setId(group.getId());
	this.setTitle(group.getTitle());
	this.type = group.getType();
    }

    public WSPerson(User user) {
	this.setId(user.getId());
	this.setTitle(user.getTitle());
	this.type = user.getType();
    }

    @Override
    public PersonTypesEnum getType() {
	return this.type;
    }

    public void setType(PersonTypesEnum type) {
	this.type = type;
    }

    @Override
    public String toString() {
	return ConversionUtils.person2String(this);
    }

    @Override
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }
}
