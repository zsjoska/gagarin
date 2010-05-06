package ro.gagarin.ws.userservice;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;

public class CreateUserOP extends WebserviceOperation {

    private final WSUser user;
    private long userId = -1;
    private AuthorizationManager authManager;
    private UserDAO userManager;
    private RoleDAO roleDAO;

    public CreateUserOP(String sessionId, WSUser user) {
	super(sessionId);
	this.user = user;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
	userManager = FACTORY.getDAOManager().getUserDAO(getSession());
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());

    }

    @Override
    public void execute() throws ExceptionBase {

	// the session user must have CREATE_USER permission
	authManager.requiresPermission(getSession(), PermissionEnum.CREATE_USER);

	// TODO: what for this rollback... no change so far
	// check user fields
	if (user.getRole() == null) {
	    userManager.markRollback();
	    throw new FieldRequiredException("ROLE", User.class);
	}

	// TODO: this logic shouldn't be here
	UserRole role = user.getRole();
	if (role.getId() == null && role.getRoleName() != null) {
	    role = roleDAO.getRoleByName(role.getRoleName());
	    if (role == null) {
		userManager.markRollback();
		throw new ItemNotFoundException(UserRole.class, user.getRole().getRoleName());
	    }
	    user.setRole(role);
	}
	// the created user's permission list must not exceed session user's
	// permissions
	authManager.checkUserRole(getSession(), user);

	this.setUserId(userManager.createUser(user));
	getApplog().info("Created User " + user.getId() + ":" + user.getUsername() + "; session:" + getSessionString());
    }

    public void setUserId(long userId) {
	this.userId = userId;
    }

    public long getUserId() {
	return userId;
    }

    @Override
    public String toString() {
	return "CreateUserOP [user=" + user + "]";
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringField("username", user, true);
    }

}
