package ro.gagarin.ws.userservice;

import java.util.List;
import java.util.Set;

import ro.gagarin.BaseControlEntity;
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

    private AuthorizationManager authManager;

    public GetRolePermissionsOP(String sessionId, WSUserRole wsUserRole) {
	super(sessionId);
	this.wsUserRole = wsUserRole;
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	// the session user must have LIST_PERMISSIONS permission
	authManager.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.LIST);

	Set<UserPermission> permissions = roleDAO.getRolePermissions(wsUserRole);
	this.permList = WSConversionUtils.convertToWSPermissionList(permissions);
    }

    public List<WSUserPermission> getRolePermissions() {
	return this.permList;
    }

    @Override
    public String toString() {
	return "GetRolePermissionstOP [wsUserRole=" + wsUserRole + "]";
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// TODO:(2) implement combined verification for id and roleName
    }

}
