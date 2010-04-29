package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserRole;

public class GetRoleListOP extends WebserviceOperation {

    private static final Statistic STAT_CREATE_USER = new Statistic("ws.userserservice.getRoleList");
    private List<WSUserRole> roles = null;

    public GetRoleListOP(String sessionId) {
	super(sessionId, GetRoleListOP.class);
    }

    @Override
    public void execute() throws ExceptionBase {
	// the session user must have LIST_ROLES permission
	FACTORY.getAuthorizationManager(getSession()).requiresPermission(getSession(), PermissionEnum.LIST_ROLES);

	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(getSession());
	List<UserRole> allRoles = roleManager.getAllRoles();
	List<WSUserRole> convRoles = new ArrayList<WSUserRole>();
	for (UserRole userRole : allRoles) {
	    convRoles.add(new WSUserRole(userRole));
	}
	this.roles = convRoles;
    }

    @Override
    public Statistic getStatistic() {
	return STAT_CREATE_USER;
    }

    public List<WSUserRole> getRoleList() {
	return this.roles;
    }

}
