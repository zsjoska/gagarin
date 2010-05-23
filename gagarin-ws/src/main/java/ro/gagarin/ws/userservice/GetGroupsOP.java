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
    private UserDAO userManager;
    private List<WSGroup> groups;

    public GetGroupsOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input to check
    }

    @Override
    public void execute() throws ExceptionBase {
	// the session user must have LIST_GROUPS permission
	authManager.requiresPermission(getSession(), BaseControlEntity.getAdminEntity(), PermissionEnum.LIST);

	groups = WSConversionUtils.convertToGroupList(userManager.getGroups());
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	userManager = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    public List<WSGroup> getGroups() {
	return this.groups;
    }

}
