package ro.gagarin.ws.userservice;

import org.apache.log4j.Logger;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;

public class CreateUserOP extends WebserviceOperation {

    private static final transient Logger LOG = Logger.getLogger(CreateUserOP.class);
    private static final Statistic STAT_CREATE_USER = new Statistic("ws.userserservice.createUser");

    private final WSUser user;
    private long userId = -1;

    public CreateUserOP(String sessionId, WSUser user) {
	super(sessionId, CreateUserOP.class);
	this.user = user;
    }

    @Override
    public void execute() throws ExceptionBase {

	AuthorizationManager authManager = FACTORY.getAuthorizationManager(getSession());

	// the session user must have CREATE_USER permission
	authManager.requiresPermission(getSession(), PermissionEnum.CREATE_USER);
	UserDAO userManager = FACTORY.getDAOManager().getUserDAO(getSession());

	// TODO: what for this rollback... no change so far
	// check user fields
	if (user.getRole() == null) {
	    userManager.markRollback();
	    throw new FieldRequiredException("ROLE", User.class);
	}

	UserRole role = user.getRole();
	if (role.getId() == null && role.getRoleName() != null) {
	    RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());
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
	LOG.info("Created User " + user.getId() + ":" + user.getUsername() + "; session:" + getSessionString());
    }

    @Override
    public Statistic getStatistic() {
	return STAT_CREATE_USER;
    }

    public void setUserId(long userId) {
	this.userId = userId;
    }

    public long getUserId() {
	return userId;
    }

}
