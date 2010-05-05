package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetAllPermissionListOP extends WebserviceOperation {

    private static final Statistic STAT_CREATE_ROLE_WITH_PERMISSIONS = Statistic
	    .getByName("ws.userserservice.getAllPermissionListOP");
    private List<WSUserPermission> permissionlist;
    private AuthorizationManager authManager;
    private RoleDAO roleManager;

    public GetAllPermissionListOP(String sessionId) {
	super(sessionId, GetAllPermissionListOP.class);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
	roleManager = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    public void execute() throws ExceptionBase {

	// the session user must have LIST_PERMISSIONS permission
	authManager.requiresPermission(getSession(), PermissionEnum.LIST_PERMISSIONS);

	List<UserPermission> allPermissions = roleManager.getAllPermissions();
	this.permissionlist = WSConversionUtils.convertToWSPermissionList(allPermissions);
    }

    @Override
    public Statistic getStatistic() {
	return STAT_CREATE_ROLE_WITH_PERMISSIONS;
    }

    public List<WSUserPermission> getPermissionList() {
	return this.permissionlist;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }
}
