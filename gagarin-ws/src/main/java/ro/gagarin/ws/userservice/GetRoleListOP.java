package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserRole;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserRole;

public class GetRoleListOP extends WebserviceOperation {

    private List<WSUserRole> roles = null;
    private RoleDAO roleDAO;

    public GetRoleListOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// the session user must have LIST permission
	authMgr.requiresPermission(session, CommonControlEntities.ADMIN_CE, PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	List<UserRole> allRoles = roleDAO.getAllRoles();
	List<WSUserRole> convRoles = new ArrayList<WSUserRole>();
	for (UserRole userRole : allRoles) {
	    convRoles.add(new WSUserRole(userRole));
	}
	this.roles = convRoles;
	getApplog().debug("Returning " + roles.size() + " roles");
    }

    public List<WSUserRole> getRoleList() {
	return this.roles;
    }
}
