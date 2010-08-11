package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermOwnerCEAssignment;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSOwner;
import ro.gagarin.ws.objects.WSPermOwnerCEAssignment;
import ro.gagarin.ws.util.WSUtil;

public class GetPermissionAssignmentsForControlEntityOP extends WebserviceOperation {

    private final WSControlEntity ce;
    private RoleDAO roleDAO;
    private UserDAO userDAO;
    private List<WSPermOwnerCEAssignment> objAssignments;

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
	authMgr.requiresPermission(session, CommonControlEntities.PERMISSION_CE, PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
	userDAO = FACTORY.getDAOManager().getUserDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	List<PermOwnerCEAssignment> assignments = roleDAO.getPermissionAssignmentsForControlEntity(this.ce);
	this.objAssignments = new ArrayList<WSPermOwnerCEAssignment>();

	// This could be a bit optimized...
	// create a map with all owners
	Map<Long, WSOwner> ownerMap = WSUtil.getOwnerMap(this.userDAO);

	// match all owners
	for (PermOwnerCEAssignment a : assignments) {
	    WSPermOwnerCEAssignment wsa = new WSPermOwnerCEAssignment(a);
	    wsa.setOwner(ownerMap.get(a.getOwner().getId()));
	    objAssignments.add(wsa);
	}
    }

    public List<WSPermOwnerCEAssignment> getPermissionAssignments() {
	return objAssignments;
    }
}
