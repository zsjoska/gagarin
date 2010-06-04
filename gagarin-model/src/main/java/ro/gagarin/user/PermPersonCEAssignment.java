package ro.gagarin.user;

import ro.gagarin.ControlEntity;
import ro.gagarin.Person;

public interface PermPersonCEAssignment {

    UserRole getRole();

    Person getPerson();

    ControlEntity getControlEntity();
}