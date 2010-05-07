package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetUserGroupsOP extends WebserviceOperation {

    private final WSUser user;
    private AuthorizationManager authManager;
    private UserDAO userManager;
    private List<WSGroup> userGoups;

    public GetUserGroupsOP(String sessionId, WSUser user) {
	super(sessionId);
	this.user = user;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	if (this.user == null) {
	    throw new FieldRequiredException("user", User.class);
	}
	if (this.user.getId() == null && this.user.getUsername() == null) {
	    throw new FieldRequiredException("id or username", User.class);
	}
	if (this.user.getId() != null) {
	    FieldValidator.requireLongField("id", this.user);
	}
	if (this.user.getUsername() != null) {
	    FieldValidator.requireStringField("username", user, true);
	}
    }

    @Override
    public void execute() throws ExceptionBase {
	// the session user must have LIST_GROUPS permission
	authManager.requiresPermission(getSession(), PermissionEnum.LIST_GROUPS);
	List<Group> groups = userManager.getUserGroups(this.user);
	this.userGoups = WSConversionUtils.convertToGroupList(groups);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
	userManager = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    public List<WSGroup> getUserGroups() {
	return this.userGoups;
    }

}
