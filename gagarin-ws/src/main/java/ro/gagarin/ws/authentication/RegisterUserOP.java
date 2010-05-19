package ro.gagarin.ws.authentication;

import ro.gagarin.ConfigurationManager;
import ro.gagarin.SessionManager;
import ro.gagarin.UserDAO;
import ro.gagarin.config.Config;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserStatus;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;

public class RegisterUserOP extends WebserviceOperation {

    private final WSUser user;
    private UserDAO userManager;
    private SessionManager sessionManager;
    private String confirmationKey;
    private ConfigurationManager cfgManager;

    public RegisterUserOP(String sessionId, WSUser user) {
	super(false, sessionId);
	this.user = user;
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
	long valid = cfgManager.getLong(Config.REGISTRATION_VALIDITY);

	user.setId(userManager.createUser(user));
	Session session = sessionManager.createSession(getSession().getLanguage(), "REGISTER", FACTORY);
	session.setUser(user);
	session.setExpires(System.currentTimeMillis() + valid);

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
