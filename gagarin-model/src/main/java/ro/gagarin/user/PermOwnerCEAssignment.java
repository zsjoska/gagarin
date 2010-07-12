package ro.gagarin.user;

import ro.gagarin.ControlEntity;
import ro.gagarin.Owner;

public interface PermOwnerCEAssignment {

    UserRole getRole();

    Owner getOwner();

    ControlEntity getControlEntity();
}