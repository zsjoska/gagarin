package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermPersonCEAssignment;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSPermPersonCEAssignment;
import ro.gagarin.ws.objects.WSPerson;
import ro.gagarin.ws.util.WSUtil;

public class GetPermissionAssignmentsForControlEntityOP extends WebserviceOperation {

    private final WSControlEntity ce;
    private RoleDAO roleDAO;
    private UserDAO userDAO;
    private List<WSPermPersonCEAssignment> objAssignments;

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
	userDAO = FACTORY.getDAOManager().getUserDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	List<PermPersonCEAssignment> assignments = roleDAO.getPermissionAssignmentsForControlEntity(this.ce);
	this.objAssignments = new ArrayList<WSPermPersonCEAssignment>();

	// This could be a bit optimized...
	// create a map with all persons
	Map<Long, WSPerson> personMap = WSUtil.getPersonMap(this.userDAO);

	// match all persons
	for (PermPersonCEAssignment a : assignments) {
	    WSPermPersonCEAssignment wsa = new WSPermPersonCEAssignment(a);
	    wsa.setPerson(personMap.get(a.getPerson().getId()));
	    objAssignments.add(wsa);
	}
    }

    public List<WSPermPersonCEAssignment> getPermissionAssignments() {
	return objAssignments;
    }
}
