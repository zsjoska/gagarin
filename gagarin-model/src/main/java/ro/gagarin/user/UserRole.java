package ro.gagarin.user;

import ro.gagarin.Entity;

//TODO:(3) Add description field

public interface UserRole extends Entity {

    Long getId();

    String getRoleName();

}