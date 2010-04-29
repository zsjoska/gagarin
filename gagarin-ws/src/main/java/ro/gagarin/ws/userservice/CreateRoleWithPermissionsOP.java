package ro.gagarin.ws.userservice;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.ConversionUtils;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;

public class CreateRoleWithPermissionsOP extends WebserviceOperation {

    private static final transient Logger LOG = Logger.getLogger(CreateUserOP.class);
    private static final Statistic STAT_CREATE_ROLE_WITH_PERMISSIONS = new Statistic(
	    "ws.userserservice.createRoleWithPermissions");

    private final String roleName;
    private final WSUserPermission[] permissions;
    private WSUserRole role;

    public CreateRoleWithPermissionsOP(String sessionId, String roleName, WSUserPermission[] permissions) {
	super(sessionId, CreateRoleWithPermissionsOP.class);
	this.roleName = roleName;
	this.permissions = permissions;
    }

    @Override
    public void execute() throws ExceptionBase {
	AuthorizationManager authManager = FACTORY.getAuthorizationManager(getSession());
	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(getSession());

	LOG.debug("Create role:" + roleName + " with permissions " + Arrays.toString(permissions));

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

    }

    @Override
    public Statistic getStatistic() {
	return STAT_CREATE_ROLE_WITH_PERMISSIONS;
    }

    public WSUserRole getRole() {
	return this.role;
    }

}
