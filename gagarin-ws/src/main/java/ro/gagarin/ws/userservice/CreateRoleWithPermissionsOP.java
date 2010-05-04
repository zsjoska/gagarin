package ro.gagarin.ws.userservice;

import java.util.Arrays;
import java.util.List;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.ConversionUtils;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;

public class CreateRoleWithPermissionsOP extends WebserviceOperation {

    private static final Statistic STAT_CREATE_ROLE_WITH_PERMISSIONS = Statistic
	    .getByName("ws.userserservice.createRoleWithPermissions");

    private final String roleName;
    private final WSUserPermission[] permissions;
    private WSUserRole role;
    private AuthorizationManager authManager;
    private RoleDAO roleManager;

    public CreateRoleWithPermissionsOP(String sessionId, String roleName, WSUserPermission[] permissions) {
	super(sessionId, CreateRoleWithPermissionsOP.class);
	this.roleName = roleName;
	this.permissions = permissions;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(session);
	roleManager = FACTORY.getDAOManager().getRoleDAO(session);
    }

    @Override
    public void execute() throws ExceptionBase {

	// the session user must have LIST_PERMISSIONS permission
	authManager.requiresPermission(getSession(), PermissionEnum.LIST_PERMISSIONS);
	List<UserPermission> allPermissions = roleManager.getAllPermissions();
	List<UserPermission> matched;
	matched = ConversionUtils.matchPermissions(allPermissions, permissions);
	authManager.checkUserHasThePermissions(getSession(), matched);

	WSUserRole role = new WSUserRole();
	role.setRoleName(roleName);
	role.setId(roleManager.createRole(role));

	for (UserPermission userPermission : matched) {
	    roleManager.assignPermissionToRole(role, userPermission);
	}

	this.role = role;
	getApplog().info("Role " + roleName + " created" + " with permissions " + Arrays.toString(permissions));

    }

    @Override
    public Statistic getStatistic() {
	return STAT_CREATE_ROLE_WITH_PERMISSIONS;
    }

    public WSUserRole getRole() {
	return this.role;
    }

    @Override
    public String toString() {
	return "CreateRoleWithPermissionsOP [permissions=" + Arrays.toString(permissions) + ", roleName=" + roleName
		+ "]";
    }

}
