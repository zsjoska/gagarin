package ro.gagarin;

/**
 * Interface declaring an object for which the access can be controlled by the
 * permission framework.<br>
 * Every Control entity should have a category which is registered and can be
 * queried.
 * 
 * @author ZsJoska
 * 
 */
public interface ControlEntity {

    Long getId();

    public String getName();

    // TODO: rename
    ControlEntityCategory getCat();

}
