package ro.gagarin.ws.userservice;

import java.util.HashSet;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.ConversionUtils;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;

public class UpdateRoleOP extends WebserviceOperation {

    private final WSUserRole role;
    private final WSUserPermission[] permissions;
    private RoleDAO roleDAO;

    public UpdateRoleOP(String sessionId, WSUserRole role, WSUserPermission[] permissions) {
	super(sessionId);
	this.role = role;
	this.permissions = permissions;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireIdField(role);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, CommonControlEntities.ROLE_CE, PermissionEnum.UPDATE);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	roleDAO.updateRole(role);

	if (this.permissions != null) {
	    HashSet<UserPermission> oldPermissions = new HashSet<UserPermission>(roleDAO.getRolePermissions(role));

	    for (UserPermission perm : this.permissions) {
		UserPermission foundPerm = ConversionUtils.findPermission(perm, oldPermissions);
		if (foundPerm == null) {
		    getApplog().debug("Add permission " + perm);
		    roleDAO.assignPermissionToRole(role, perm);
		} else {
		    oldPermissions.remove(foundPerm);
		}
	    }
	    for (UserPermission perm : oldPermissions) {
		getApplog().debug("Remove permission " + perm);
		roleDAO.unAssignPermissionFromRole(role, perm);
	    }
	}

    }
}
