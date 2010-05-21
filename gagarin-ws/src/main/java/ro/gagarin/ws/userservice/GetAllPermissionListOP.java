package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserPermission;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetAllPermissionListOP extends WebserviceOperation {

    private List<WSUserPermission> permissionlist;
    private AuthorizationManager authManager;
    private RoleDAO roleManager;

    public GetAllPermissionListOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	roleManager = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    public void execute() throws ExceptionBase {

	// the session user must have LIST_PERMISSIONS permission
	authManager.requiresPermission(getSession(), PermissionEnum.LIST_PERMISSIONS);

	List<UserPermission> allPermissions = roleManager.getAllPermissions();
	this.permissionlist = WSConversionUtils.convertToWSPermissionList(allPermissions);
    }

    public List<WSUserPermission> getPermissionList() {
	return this.permissionlist;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }
}
