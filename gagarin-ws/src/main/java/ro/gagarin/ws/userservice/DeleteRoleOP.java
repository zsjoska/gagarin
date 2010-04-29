package ro.gagarin.ws.userservice;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserRole;

public class DeleteRoleOP extends WebserviceOperation {

    private static final Statistic STAT_DELETE_ROLE = new Statistic("ws.userserservice.deleteRole");

    private final WSUserRole role;

    public DeleteRoleOP(String sessionId, WSUserRole role) {
	super(sessionId, DeleteRoleOP.class);
	this.role = role;
    }

    @Override
    public void execute() throws ExceptionBase {
	AuthorizationManager authManager = FACTORY.getAuthorizationManager(getSession());

	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(getSession());

	// the session user must have LIST_PERMISSIONS permission
	authManager.requiresPermission(getSession(), PermissionEnum.DELETE_ROLE);

	roleManager.deleteRole(role);
    }

    @Override
    public Statistic getStatistic() {
	return STAT_DELETE_ROLE;
    }

}
