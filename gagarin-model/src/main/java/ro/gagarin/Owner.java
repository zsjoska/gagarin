package ro.gagarin;

import ro.gagarin.user.Group;
import ro.gagarin.user.User;

/**
 * Interface defining a structure which can have a role on a control object.<br>
 * An owner could be a {@link Group} or a {@link User} object.
 * 
 * @author zsjoska
 * 
 */
public interface Owner extends Entity {

    Long getId();

    OwnerTypesEnum getType();

    String getTitle();
}
