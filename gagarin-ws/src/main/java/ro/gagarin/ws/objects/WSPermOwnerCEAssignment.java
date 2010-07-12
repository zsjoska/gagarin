package ro.gagarin.ws.objects;

import ro.gagarin.ControlEntity;
import ro.gagarin.Owner;
import ro.gagarin.user.PermOwnerCEAssignment;
import ro.gagarin.user.UserRole;

public class WSPermOwnerCEAssignment implements PermOwnerCEAssignment {

    private WSControlEntity controlEntity;
    private WSOwner owner;
    private WSUserRole role;

    public WSPermOwnerCEAssignment() {
    }

    public WSPermOwnerCEAssignment(PermOwnerCEAssignment assignment) {
	this.setControlEntity(assignment.getControlEntity());
	this.setOwner(assignment.getOwner());
	this.setRole(assignment.getRole());
    }

    @Override
    public WSControlEntity getControlEntity() {
	return this.controlEntity;
    }

    @Override
    public WSOwner getOwner() {
	return this.owner;
    }

    @Override
    public WSUserRole getRole() {
	return this.role;
    }

    public void setControlEntity(ControlEntity controlEntity) {
	this.controlEntity = new WSControlEntity(controlEntity);
    }

    public void setControlEntity(WSControlEntity controlEntity) {
	this.controlEntity = controlEntity;
    }

    public void setOwner(Owner owner) {
	this.owner = new WSOwner(owner);
    }

    public void setOwner(WSOwner owner) {
	this.owner = owner;
    }

    public void setRole(UserRole role) {
	this.role = new WSUserRole(role);
    }

    public void setRole(WSUserRole role) {
	this.role = role;
    }
}
