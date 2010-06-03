package ro.gagarin.jdbc.objects;

import ro.gagarin.ControlEntity;
import ro.gagarin.Person;
import ro.gagarin.user.PermPersonCEAssignment;
import ro.gagarin.user.UserRole;

public class DBPermPersonCEAssignment implements PermPersonCEAssignment {

    private ControlEntity controlEntity;
    private Person person;
    private UserRole role;

    @Override
    public ControlEntity getControlEntity() {
	return this.controlEntity;
    }

    @Override
    public Person getPerson() {
	return this.person;
    }

    @Override
    public UserRole getRole() {
	return this.role;
    }

    public void setControlEntity(ControlEntity controlEntity) {
	this.controlEntity = controlEntity;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public void setRole(UserRole role) {
	this.role = role;
    }
}
