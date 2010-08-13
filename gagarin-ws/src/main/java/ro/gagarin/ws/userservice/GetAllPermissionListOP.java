package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.PermissionTest;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserPermission;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetAllPermissionListOP extends WebserviceOperation {

    private List<WSUserPermission> permissionlist;
    private RoleDAO roleDAO;

    public GetAllPermissionListOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, new PermissionTest(CommonControlEntities.ROLE_CE, PermissionEnum.LIST),
		new PermissionTest(CommonControlEntities.PERMISSION_CE, PermissionEnum.LIST));
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	List<UserPermission> allPermissions = roleDAO.getAllPermissions();
	this.permissionlist = WSConversionUtils.convertToWSPermissionList(allPermissions);
	getApplog().debug("Returning " + permissionlist.size() + " permissions");
    }

    public List<WSUserPermission> getPermissionList() {
	return this.permissionlist;
    }
}
