package ro.gagarin.user;

import ro.gagarin.ControlEntityCategory;

public interface ControlEntity {

    void setId(Long adminControlEntityId);

    void setName(String string);

    Long getId();

    // TODO: rename
    ControlEntityCategory getCat();

}
