package ro.gagarin.ws.userservice;

import java.util.List;
import java.util.Set;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetRolePermissionstOP extends WebserviceOperation {

    private static final Statistic STAT_GET_ROLE_PERMISSIONLIST = new Statistic(
	    "ws.userserservice.getRolePermissionstOP");

    private final WSUserRole wsUserRole;

    private List<WSUserPermission> permList;

    public GetRolePermissionstOP(String sessionId, WSUserRole wsUserRole) {
	super(sessionId, GetRolePermissionstOP.class);
	this.wsUserRole = wsUserRole;
    }

    @Override
    public void execute() throws ExceptionBase {
	AuthorizationManager authManager = FACTORY.getAuthorizationManager(getSession());

	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(getSession());

	// the session user must have LIST_PERMISSIONS permission
	authManager.requiresPermission(getSession(), PermissionEnum.LIST_PERMISSIONS);

	Set<UserPermission> permissions = roleManager.getRolePermissions(wsUserRole);
	this.permList = WSConversionUtils.convertToWSPermissionList(permissions);
    }

    @Override
    public Statistic getStatistic() {
	return STAT_GET_ROLE_PERMISSIONLIST;
    }

    public List<WSUserPermission> getRolePermissions() {
	return this.permList;
    }

}
