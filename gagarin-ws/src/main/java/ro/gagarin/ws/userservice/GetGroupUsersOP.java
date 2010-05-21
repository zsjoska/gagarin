package ro.gagarin.ws.userservice;

import java.security.acl.Group;
import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetGroupUsersOP extends WebserviceOperation {

    private final WSGroup group;
    private AuthorizationManager authManager;
    private UserDAO userManager;
    private List<WSUser> groupUsers;

    public GetGroupUsersOP(String sessionId, WSGroup group) {
	super(sessionId);
	this.group = group;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	if (this.group == null) {
	    throw new FieldRequiredException("group", Group.class);
	}
	if (this.group.getId() == null && this.group.getName() == null) {
	    throw new FieldRequiredException("id or name", Group.class);
	}
	if (this.group.getId() != null) {
	    FieldValidator.requireLongField("id", this.group);
	}
	if (this.group.getName() != null) {
	    FieldValidator.requireStringField("name", group, true);
	}

    }

    @Override
    public void execute() throws ExceptionBase {
	// the session user must have LIST_USERS permission
	authManager.requiresPermission(getSession(), PermissionEnum.LIST, BaseControlEntity.getAdminEntity());
	List<User> users = userManager.getGroupUsers(group);
	this.groupUsers = WSConversionUtils.convertToWSUserList(users);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	userManager = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    public List<WSUser> getGroupUsers() {
	return this.groupUsers;
    }

}
