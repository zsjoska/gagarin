package ro.gagarin.ws.userservice;

import java.security.acl.Group;
import java.util.List;

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
    private UserDAO userDAO;
    private List<WSUser> groupUsers;

    public GetGroupUsersOP(String sessionId, WSGroup group) {
	super(sessionId);
	this.group = group;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
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
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// the session user must have LIST permission on group
	authMgr.requiresPermission(session, this.group, PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	List<User> users = userDAO.getGroupUsers(group);
	this.groupUsers = WSConversionUtils.convertToWSUserList(users);
	getApplog().debug("Returning " + groupUsers.size() + " users");
    }

    public List<WSUser> getGroupUsers() {
	return this.groupUsers;
    }
}
