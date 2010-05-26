package ro.gagarin.user;

import ro.gagarin.Person;

public interface User extends Person {

    public abstract String getUsername();

    public abstract String getPassword();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getPhone();

    public abstract Long getId();

    public abstract UserStatus getStatus();

    public abstract AuthenticationType getAuthentication();

    public abstract Long getCreated();

}