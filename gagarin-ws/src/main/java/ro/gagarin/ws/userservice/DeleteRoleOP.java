package ro.gagarin.ws.userservice;

import ro.gagarin.BaseControlEntity;
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

    private AuthorizationManager authManager;

    private RoleDAO roleDAO;

    // TODO:(2) delete also by role name
    public DeleteRoleOP(String sessionId, WSUserRole role) {
	super(sessionId);
	this.role = role;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    public void execute() throws ExceptionBase {

	authManager.requiresPermission(getSession(), BaseControlEntity.getAdminEntity(), PermissionEnum.DELETE);

	roleDAO.deleteRole(role);
	getApplog().info("Role " + role.getRoleName() + " deleted");
    }

    @Override
    public String toString() {
	return "DeleteRoleOP [role=" + role + "]";
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireLongField("id", role);
    }
}
