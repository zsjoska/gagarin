package ro.gagarin.ws.userservice;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserRole;

public class DeleteRoleOP extends WebserviceOperation {

    private static final Statistic STAT_DELETE_ROLE = Statistic.getByName("ws.userserservice.deleteRole");

    private final WSUserRole role;

    private AuthorizationManager authManager;

    private RoleDAO roleManager;

    public DeleteRoleOP(String sessionId, WSUserRole role) {
	super(sessionId, DeleteRoleOP.class);
	this.role = role;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
	roleManager = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    public void execute() throws ExceptionBase {

	// the session user must have LIST_PERMISSIONS permission
	authManager.requiresPermission(getSession(), PermissionEnum.DELETE_ROLE);

	roleManager.deleteRole(role);
	getApplog().info("Role " + role.getRoleName() + " deleted");
    }

    @Override
    public Statistic getStatistic() {
	return STAT_DELETE_ROLE;
    }

    @Override
    public String toString() {
	return "DeleteRoleOP [role=" + role + "]";
    }

}
