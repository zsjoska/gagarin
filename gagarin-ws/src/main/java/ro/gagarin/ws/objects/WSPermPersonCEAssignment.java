package ro.gagarin.ws.objects;

import ro.gagarin.ControlEntity;
import ro.gagarin.Person;
import ro.gagarin.user.PermPersonCEAssignment;
import ro.gagarin.user.UserRole;

public class WSPermPersonCEAssignment implements PermPersonCEAssignment {

    private WSControlEntity controlEntity;
    private WSPerson person;
    private WSUserRole role;

    public WSPermPersonCEAssignment() {
    }

    public WSPermPersonCEAssignment(PermPersonCEAssignment assignment) {
	this.setControlEntity(assignment.getControlEntity());
	this.setPerson(assignment.getPerson());
	this.setRole(assignment.getRole());
    }

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
	this.controlEntity = new WSControlEntity(controlEntity);
    }

    public void setControlEntity(WSControlEntity controlEntity) {
	this.controlEntity = controlEntity;
    }

    public void setPerson(Person person) {
	this.person = new WSPerson(person);
    }

    public void setRole(UserRole role) {
	this.role = new WSUserRole(role);
    }
}
