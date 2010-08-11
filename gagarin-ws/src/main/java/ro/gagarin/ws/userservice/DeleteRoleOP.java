package ro.gagarin.ws.userservice;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserRole;

public class DeleteRoleOP extends WebserviceOperation {

    private final WSUserRole role;

    private RoleDAO roleDAO;

    // TODO:(2) delete also by role name
    public DeleteRoleOP(String sessionId, WSUserRole role) {
	super(sessionId);
	this.role = role;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireLongField("id", role);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(getSession(), CommonControlEntities.ADMIN_CE, PermissionEnum.DELETE);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	roleDAO.deleteRole(role);
	getApplog().info("Role " + role.getRoleName() + " deleted");
    }

    @Override
    public String toString() {
	return "DeleteRoleOP [role=" + role + "]";
    }
}
