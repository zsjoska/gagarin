package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;

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
    private AuthorizationManager authorizationManager;
    private RoleDAO roleManager;

    public GetRoleListOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authorizationManager = FACTORY.getAuthorizationManager();
	roleManager = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    public void execute() throws ExceptionBase {
	// the session user must have LIST_ROLES permission
	authorizationManager.requiresPermission(getSession(), PermissionEnum.LIST_ROLES);

	List<UserRole> allRoles = roleManager.getAllRoles();
	List<WSUserRole> convRoles = new ArrayList<WSUserRole>();
	for (UserRole userRole : allRoles) {
	    convRoles.add(new WSUserRole(userRole));
	}
	this.roles = convRoles;
    }

    public List<WSUserRole> getRoleList() {
	return this.roles;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }
}
