package ro.gagarin.user;

import ro.gagarin.Owner;

public interface User extends Owner {

    public abstract String getUsername();

    public abstract String getPassword();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getPhone();

    public abstract Long getId();

    public abstract UserStatus getStatus();

    public abstract String getAuthentication();

    public abstract Long getCreated();

}