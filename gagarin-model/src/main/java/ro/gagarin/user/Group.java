package ro.gagarin.user;

import ro.gagarin.ControlEntity;
import ro.gagarin.Owner;

public interface Group extends Owner, ControlEntity {

    public abstract String getName();

    public abstract String getDescription();

    public abstract Long getId();

}