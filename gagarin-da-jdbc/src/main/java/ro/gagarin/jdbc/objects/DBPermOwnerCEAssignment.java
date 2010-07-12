package ro.gagarin.jdbc.objects;

import ro.gagarin.ControlEntity;
import ro.gagarin.Owner;
import ro.gagarin.user.PermOwnerCEAssignment;
import ro.gagarin.user.UserRole;

public class DBPermOwnerCEAssignment implements PermOwnerCEAssignment {

    private ControlEntity controlEntity;
    private Owner owner;
    private UserRole role;

    @Override
    public ControlEntity getControlEntity() {
	return this.controlEntity;
    }

    @Override
    public Owner getOwner() {
	return this.owner;
    }

    @Override
    public UserRole getRole() {
	return this.role;
    }

    public void setControlEntity(ControlEntity controlEntity) {
	this.controlEntity = controlEntity;
    }

    public void setOwner(Owner owner) {
	this.owner = owner;
    }

    public void setRole(UserRole role) {
	this.role = role;
    }
}
