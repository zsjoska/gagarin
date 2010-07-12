package ro.gagarin.ws.userservice;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSOwner;
import ro.gagarin.ws.objects.WSUserRole;

public class UnAssignRoleFromControlEntityOP extends WebserviceOperation {

    private final WSControlEntity ce;
    private final WSUserRole role;
    private final WSOwner owner;
    private RoleDAO roleDAO;

    public UnAssignRoleFromControlEntityOP(String sessionId, WSControlEntity ce, WSUserRole role, WSOwner owner) {
	super(sessionId);
	this.ce = ce;
	this.role = role;
	this.owner = owner;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireLongField("id", ce);
	FieldValidator.requireLongField("id", role);
	FieldValidator.requireLongField("id", owner);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// TODO:(2) review if this is OK: Admin can un-assign any role
	authMgr.requiresPermission(session, ce, PermissionEnum.ADMIN);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	roleDAO.unAssignRoleFromOwner(role, owner, ce);

    }
}
