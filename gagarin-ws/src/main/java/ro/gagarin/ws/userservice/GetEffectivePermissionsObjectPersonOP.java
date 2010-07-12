package ro.gagarin.ws.userservice;

import java.util.List;
import java.util.Set;

import ro.gagarin.config.Config;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSPerson;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.util.WSUtil;

public class GetEffectivePermissionsObjectPersonOP extends WebserviceOperation {

    private final WSControlEntity ce;
    private final WSPerson person;
    private RoleDAO roleDAO;
    private Set<PermissionEnum> permissions;
    private ConfigurationManager cfgMgr;
    private UserDAO userDAO;

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
	userDAO = session.getManagerFactory().getDAOManager().getUserDAO(session);
	cfgMgr = session.getManagerFactory().getConfigurationManager();
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	permissions = null;
	// if the person is admin or admin group
	// or is a member of the admin group, we have to return all permissions

	String adminGroupName = cfgMgr.getString(Config.ADMIN_GROUP_NAME);
	// TODO:(5) Subject for cache
	Group adminGroup = userDAO.getGroupByName(adminGroupName);
	if (adminGroup.getId().equals(person.getId())) {
	    this.permissions = WSUtil.createAllPermissionSet();
	} else {
	    WSUser maybeAuser = new WSUser();
	    maybeAuser.setId(person.getId());
	    List<Group> userGroups = userDAO.getUserGroups(maybeAuser);
	    for (Group group : userGroups) {
		if (group.getId().equals(adminGroup.getId())) {
		    this.permissions = WSUtil.createAllPermissionSet();
		    break;
		}
	    }
	}
	if (permissions == null) {
	    this.permissions = roleDAO.getEffectivePermissionsObjectPerson(ce, person);
	}
    }

    public Set<PermissionEnum> getPermissions() {
	return this.permissions;
    }
}