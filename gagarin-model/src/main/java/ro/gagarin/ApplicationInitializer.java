package ro.gagarin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.gagarin.config.Config;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.session.Session;
import ro.gagarin.user.DBUser;
import ro.gagarin.user.DBUserPermission;
import ro.gagarin.user.DBUserRole;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class ApplicationInitializer {

	private static final transient Logger LOG = Logger.getLogger(ApplicationInitializer.class);

	private static boolean initRun = false;

	private final ConfigurationManager cfgManager;

	private final UserDAO userManager;

	private final RoleDAO roleManager;

	private String task;

	public ApplicationInitializer(ConfigurationManager cfgManager, UserDAO userManager,
			RoleDAO roleManager) {
		this.cfgManager = cfgManager;
		this.userManager = userManager;
		this.roleManager = roleManager;
	}

	public static synchronized boolean init() {

		if (initRun)
			return true;
		initRun = true;

		LOG.info("Application initializer started");

		Session session = new Session();

		UserDAO userManager = ModelFactory.getDAOManager().getUserDAO(session);
		ConfigurationManager cfgManager = ModelFactory.getConfigurationManager(session);
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);

		ApplicationInitializer initializer = new ApplicationInitializer(cfgManager, userManager,
				roleManager);
		try {
			initializer.doInit();
		} catch (Exception e) {
			LOG.error("Application initializer failed for task:" + initializer.getTask(), e);
			throw new RuntimeException(e);
		} finally {
			roleManager.release();
			userManager.release();
			cfgManager.release();
		}

		LOG.info("Application initializer finished");
		return true;
	}

	private void doInit() throws FieldRequiredException, UserAlreadyExistsException {
		this.setTask("GET_CFG_ADMIN_ROLE");
		String adminRoleName = cfgManager.getString(Config.ADMIN_ROLE_NAME);

		this.setTask("CHK_CREATE_PERMISSIONLIST");
		checkCreatePermissionList();

		this.setTask("CHK_CREATE_ADMIN_ROLE");
		DBUserRole adminRole = checkCreateAdminRole(adminRoleName);

		this.setTask("CHK_CREATE_ADMIN_ROLE_PERMISSION_LIST");
		checkAdminRolePermissionList(adminRole);

		this.setTask("CHK_CREATE_ADMIN_USERS");
		checkAdminUsers(adminRole);

		this.setTask(null);

	}

	private DBUserRole checkCreateAdminRole(String adminRoleName) {
		LOG.info("Checking admin role existence");
		DBUserRole adminRole = roleManager.getRoleByName(adminRoleName);
		if (adminRole == null) {
			LOG.info("No admin role was found, creating role with " + adminRoleName);
			adminRole = new DBUserRole();
			adminRole.setRoleName(adminRoleName);
			roleManager.createRole(adminRole);
			LOG.info("Admin role created.");
		}
		return adminRole;
	}

	private void checkAdminUsers(UserRole adminRole) throws FieldRequiredException,
			UserAlreadyExistsException {
		LOG.info("Checking admin user");
		String adminUserName = cfgManager.getString(Config.ADMIN_USER_NAME);
		String adminPassword = cfgManager.getString(Config.ADMIN_PASSWORD);
		List<User> adminUsers = userManager.getUsersWithRole(adminRole);
		if (adminUsers == null || adminUsers.size() == 0) {
			LOG.info("admin user was not found; creating");
			DBUser adminUser = null;
			adminUser = new DBUser();
			adminUser.setRole(adminRole);
			adminUser.setUsername(adminUserName);
			adminUser.setPassword(adminPassword);
			adminUser.setName("Gagarin");
			userManager.createUser(adminUser);
			if (adminUsers == null)
				adminUsers = new ArrayList<User>(1);
			adminUsers.add(adminUser);
		}
	}

	private void checkAdminRolePermissionList(DBUserRole adminRole) {
		LOG.info("Checking AdminRolePermissionList to include all permissions");
		Set<DBUserPermission> grantedPermissions = adminRole.getUserPermissions();
		List<DBUserPermission> permissions = roleManager.getAllPermissions();
		if (permissions == null || permissions.size() == 0) {
			throw new RuntimeException(
					"Inconsistent state: The permission list should have been created!");
		}
		for (DBUserPermission userPermission : permissions) {
			if (!grantedPermissions.contains(userPermission)) {
				LOG.info("Adding permission " + userPermission.getPermissionName()
						+ " to admin role");
				grantedPermissions.add(userPermission);
				userPermission.getUserRoles().add(adminRole);
			} else {
				LOG.info("Already assigned " + userPermission.getPermissionName()
						+ " to admin role");
			}
		}
	}

	private void checkCreatePermissionList() {
		LOG.info("Checking Permission List");
		PermissionEnum[] values = PermissionEnum.values();
		for (PermissionEnum permission : values) {

			if (roleManager.getPermissionByName(permission.name()) == null) {
				DBUserPermission perm = new DBUserPermission();
				perm.setPermissionName(permission.name());
				roleManager.createPermission(perm);
			} else {
				LOG.debug("Permission was found:" + permission.name());
			}
		}
	}

	public void setTask(String task) {
		this.task = task;
		LOG.info("Executing task " + task);
	}

	public String getTask() {
		return task;
	}
}
