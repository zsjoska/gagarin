package ro.gagarin.user;

import ro.gagarin.Person;

public interface Group extends Person {

    public abstract String getName();

    public abstract String getDescription();

    public abstract Long getId();

}