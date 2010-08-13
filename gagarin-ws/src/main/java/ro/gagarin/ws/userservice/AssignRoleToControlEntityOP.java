package ro.gagarin.ws.userservice;

import java.util.ArrayList;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.PermissionTest;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSOwner;
import ro.gagarin.ws.objects.WSUserRole;
import ro.gagarin.ws.util.WSUtil;

public class AssignRoleToControlEntityOP extends WebserviceOperation {

    private final WSControlEntity ce;
    private final WSUserRole role;
    private final WSOwner owner;
    private RoleDAO roleDAO;
    private UserDAO userDAO;

    public AssignRoleToControlEntityOP(String sessionId, WSControlEntity ce, WSUserRole role, WSOwner owner) {
	super(sessionId);
	this.ce = ce;
	this.role = role;
	this.owner = owner;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireLongField("id", ce);
	FieldValidator.requireLongField("id", role);
	FieldValidator.requireLongField("id", owner);

	// TODO:(4) the current AdminUI does not know the type so we look up it
	// FieldValidator.requireField("type", this.owner);

	FieldValidator.requireField("category", this.ce);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, new PermissionTest(ce, PermissionEnum.ADMIN), new PermissionTest(
		CommonControlEntities.PERMISSION_CE, PermissionEnum.ADMIN));
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
	userDAO = session.getManagerFactory().getDAOManager().getUserDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	// the type is required so we need to look up if it is missing
	if (owner.getType() == null) {
	    ArrayList<WSOwner> ownersList = WSUtil.getOwnersList(userDAO);
	    for (WSOwner wsOwner : ownersList) {
		if (wsOwner.getId().equals(owner.getId())) {
		    owner.setType(wsOwner.getType());
		    break;
		}
	    }
	}
	FieldValidator.requireField("type", this.owner);

	roleDAO.assignRoleToOwner(role, owner, ce);

    }
}
