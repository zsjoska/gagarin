package ro.gagarin.exceptions;

import org.apache.log4j.Logger;

public class UserNotFoundException extends ExceptionBase {
	private static final transient Logger LOG = Logger.getLogger(UserNotFoundException.class);

	private final String username;

	public UserNotFoundException(String username) {
		super(ErrorCodes.USER_NOT_FOUND);
		this.username = username;
		LOG.error("User " + username + " was not found");
	}

	public String getUsername() {
		return username;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5953812143091133972L;

}
