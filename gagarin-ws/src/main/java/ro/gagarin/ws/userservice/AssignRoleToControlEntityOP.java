package ro.gagarin.ws.userservice;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSPerson;
import ro.gagarin.ws.objects.WSUserRole;

public class AssignRoleToControlEntityOP extends WebserviceOperation {

    private final WSControlEntity ce;
    private final WSUserRole role;
    private final WSPerson person;
    private RoleDAO roleDAO;

    public AssignRoleToControlEntityOP(String sessionId, WSControlEntity ce, WSUserRole role, WSPerson person) {
	super(sessionId);
	this.ce = ce;
	this.role = role;
	this.person = person;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireLongField("id", ce);
	FieldValidator.requireLongField("id", role);
	FieldValidator.requireLongField("id", person);
	FieldValidator.requireField("type", this.person);
	FieldValidator.requireField("category", this.ce);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// TODO:(2) review if this is OK: Admin can assign any role
	authMgr.requiresPermission(session, ce, PermissionEnum.ADMIN);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	roleDAO.assignRoleToPerson(role, person, ce);

    }
}
