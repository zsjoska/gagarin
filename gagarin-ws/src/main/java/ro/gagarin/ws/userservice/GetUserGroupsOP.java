package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.manager.AuthorizationManager;
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
    private UserDAO userDAO;
    private List<WSGroup> userGoups;

    public GetUserGroupsOP(String sessionId, WSUser user) {
	super(sessionId);
	this.user = user;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
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
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// the session user must have LIST permission
	authMgr.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	List<Group> groups = userDAO.getUserGroups(this.user);
	this.userGoups = WSConversionUtils.convertToGroupList(groups);
    }

    public List<WSGroup> getUserGroups() {
	return this.userGoups;
    }
}
