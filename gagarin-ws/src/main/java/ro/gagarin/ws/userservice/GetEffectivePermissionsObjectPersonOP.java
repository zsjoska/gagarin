package ro.gagarin.ws.userservice;

import java.util.Set;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSPerson;

public class GetEffectivePermissionsObjectPersonOP extends WebserviceOperation {

    private final WSControlEntity ce;
    private final WSPerson person;
    private RoleDAO roleDAO;
    private Set<PermissionEnum> permissions;

    public GetEffectivePermissionsObjectPersonOP(String sessionId, WSControlEntity ce, WSPerson person) {
	super(sessionId);
	this.ce = ce;
	this.person = person;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {

	// TODO:(4) Enable use without ID

	FieldValidator.requireIdField(ce);
	FieldValidator.requireIdField(person);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// The intent of this operation is to show an unified list of all
	// permission that a person has on an object
	// The requesting user must have LIST permission on the object
	authMgr.requiresPermission(session, ce, PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	this.permissions = roleDAO.getEffectivePermissionsObjectPerson(ce, person);
    }

    public Set<PermissionEnum> getPermissions() {
	return this.permissions;
    }
}