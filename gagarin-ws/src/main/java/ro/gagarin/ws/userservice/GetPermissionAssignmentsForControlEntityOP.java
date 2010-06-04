package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermPersonCEAssignment;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSPermPersonCEAssignment;

public class GetPermissionAssignmentsForControlEntityOP extends WebserviceOperation {

    private final WSControlEntity ce;
    private RoleDAO roleDAO;

    public GetPermissionAssignmentsForControlEntityOP(String sessionId, WSControlEntity ce) {
	super(sessionId);
	this.ce = ce;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireIdField(ce);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.ADMIN);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	List<PermPersonCEAssignment> assignments = roleDAO.getPermissionAssignmentsForControlEntity(this.ce);

    }

    public List<WSPermPersonCEAssignment> getPermissionAssignments() {
	// TODO Auto-generated method stub
	return null;
    }

}
