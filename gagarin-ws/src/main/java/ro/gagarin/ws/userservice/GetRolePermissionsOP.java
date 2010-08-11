package ro.gagarin.ws.userservice;

import java.util.List;
import java.util.Set;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserPermission;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetRolePermissionsOP extends WebserviceOperation {

    private final WSUserRole wsUserRole;

    private List<WSUserPermission> permList;

    private RoleDAO roleDAO;

    public GetRolePermissionsOP(String sessionId, WSUserRole wsUserRole) {
	super(sessionId);
	this.wsUserRole = wsUserRole;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// TODO:(2) implement combined verification for id and roleName
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// the session user must have LIST permission
	authMgr.requiresPermission(session, CommonControlEntities.ADMIN_CE, PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	Set<UserPermission> permissions = roleDAO.getRolePermissions(wsUserRole);
	this.permList = WSConversionUtils.convertToWSPermissionList(permissions);
	getApplog().debug("Returning " + permList.size() + " permissions");
    }

    public List<WSUserPermission> getRolePermissions() {
	return this.permList;
    }

    @Override
    public String toString() {
	return "GetRolePermissionstOP [wsUserRole=" + wsUserRole + "]";
    }
}
