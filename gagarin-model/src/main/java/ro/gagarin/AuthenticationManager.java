package ro.gagarin;

import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.user.User;

public interface AuthenticationManager {

	User userLogin(String username, String password, String[] extra) throws ItemNotFoundException;
}
