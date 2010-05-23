package ro.gagarin.ws.authentication;

import ro.gagarin.config.Config;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.manager.SessionManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.user.UserStatus;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSUser;

public class RegisterUserOP extends WebserviceOperation {

    private final WSUser user;
    private UserDAO userManager;
    private SessionManager sessionManager;
    private String confirmationKey;
    private ConfigurationManager cfgManager;
    private final String defGroupname;

    public RegisterUserOP(String sessionId, WSUser user, String defGroupname) {
	super(false, sessionId);
	this.user = user;
	this.defGroupname = defGroupname;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	cfgManager = FACTORY.getConfigurationManager();
	boolean registration = cfgManager.getBoolean(Config.ALLOW_USER_REGISTRATION);
	if (!registration) {
	    throw new OperationException(ErrorCodes.FEATURE_DISABLED, "User registration is not allowed");
	}
	FieldValidator.requireStringField("username", user, true);
	FieldValidator.requireStringField("password", user, true);
	FieldValidator.requireStringField("email", user, true);
	user.setStatus(UserStatus.INIT);
	// TODO: verify that admin is not permitted to register
    }

    @Override
    public void execute() throws ExceptionBase {
	User sessionUser = this.user;
	long valid = cfgManager.getLong(Config.REGISTRATION_VALIDITY);

	User userByUsername = userManager.getUserByUsername(user.getUsername());
	if (userByUsername != null && userByUsername.getStatus() != UserStatus.INIT) {
	    throw new ItemExistsException(User.class, "username", null);
	}
	if (userByUsername != null) {
	    sessionUser = userByUsername;
	} else {
	    // inexistent user, we will create it
	    user.setId(userManager.createUser(user));
	}

	// assign to the default group
	if (this.defGroupname != null) {
	    Group group = this.userManager.getGroupByName(defGroupname);
	    if (group == null) {
		WSGroup newGroup = new WSGroup();
		newGroup.setName(defGroupname);
		newGroup.setDescription("automatically created");
		newGroup.setId(userManager.createGroup(newGroup));
		group = newGroup;
	    }
	    userManager.assignUserToGroup(sessionUser, group);
	}

	Session session = sessionManager.createSession(getSession().getLanguage(), "REGISTER", FACTORY);
	session.setUser(sessionUser);
	session.setExpires(System.currentTimeMillis() + valid);

	// TODO: Add notification call

	this.confirmationKey = session.getSessionString();
	getApplog().info("Registration key " + this.confirmationKey + " assigned for user " + user);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	sessionManager = FACTORY.getSessionManager();
	userManager = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    public String getConfirmationKey() {
	return this.confirmationKey;
    }

}