package ro.gagarin.user;

import ro.gagarin.Entity;

public interface UserPermission extends Entity {

    Long getId();

    String getPermissionName();

}