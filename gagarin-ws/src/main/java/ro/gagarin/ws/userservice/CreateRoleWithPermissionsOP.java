package ro.gagarin.ws.userservice;

import java.util.Arrays;
import java.util.List;

import ro.gagarin.BaseControlEntity;
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

public class CreateRoleWithPermissionsOP extends WebserviceOperation {

    private final String roleName;
    private final WSUserPermission[] permissions;
    private WSUserRole role;
    private AuthorizationManager authManager;
    private RoleDAO roleManager;

    public CreateRoleWithPermissionsOP(String sessionId, String roleName, WSUserPermission[] permissions) {
	super(sessionId);
	this.roleName = roleName;
	this.permissions = permissions;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	roleManager = FACTORY.getDAOManager().getRoleDAO(session);
    }

    @Override
    public void execute() throws ExceptionBase {

	authManager.requiresPermission(getSession(), BaseControlEntity.getAdminEntity(), PermissionEnum.CREATE);

	List<UserPermission> allPermissions = roleManager.getAllPermissions();
	List<UserPermission> matched;
	matched = ConversionUtils.matchPermissions(allPermissions, permissions);

	WSUserRole role = new WSUserRole();
	role.setRoleName(roleName);
	role.setId(roleManager.createRole(role));

	for (UserPermission userPermission : matched) {
	    roleManager.assignPermissionToRole(role, userPermission);
	}

	this.role = role;
	getApplog().info("Role " + roleName + " created" + " with permissions " + Arrays.toString(permissions));

    }

    public WSUserRole getRole() {
	return this.role;
    }

    @Override
    public String toString() {
	return "CreateRoleWithPermissionsOP [permissions=" + Arrays.toString(permissions) + ", roleName=" + roleName
		+ "]";
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringValue(roleName, "roleName", 50);
	// TODO:(2) check permissions
    }

}
