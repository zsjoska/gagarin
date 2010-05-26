package ro.gagarin;

import ro.gagarin.user.Group;
import ro.gagarin.user.User;

/**
 * Interface defining a structure which can have a role on a control object.<br>
 * A person could be a {@link Group} or a {@link User} object.
 * 
 * @author zsjoska
 * 
 */
public interface Person {

    Long getId();

    PersonTypesEnum getType();

    String getTitle();
}
