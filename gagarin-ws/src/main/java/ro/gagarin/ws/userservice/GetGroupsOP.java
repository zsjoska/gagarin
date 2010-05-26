package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetGroupsOP extends WebserviceOperation {

    private AuthorizationManager authManager;
    private UserDAO userDAO;
    private List<WSGroup> groups;

    public GetGroupsOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input to check
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	// the session user must have LIST_GROUPS permission
	authManager.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.LIST);

	groups = WSConversionUtils.convertToGroupList(userDAO.getGroups());
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    public List<WSGroup> getGroups() {
	return this.groups;
    }

}
