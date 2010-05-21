package ro.gagarin.ws.authentication;

import java.util.Set;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetCurrentUserPermissionsOP extends WebserviceOperation {

    private Set<WSUserPermission> currentUserPermissions;
    private RoleDAO roleDAO;

    public GetCurrentUserPermissionsOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());

    }

    @Override
    public void execute() throws ExceptionBase {

	Set<UserPermission> perm = roleDAO.getRolePermissions(getSession().getUser().getRole());
	this.currentUserPermissions = WSConversionUtils.convertToWSPermissionSet(perm);

    }

    public Set<WSUserPermission> getCurrentUserPermissions() {
	return currentUserPermissions;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }

}
