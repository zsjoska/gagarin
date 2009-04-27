package ro.gagarin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.gagarin.config.Config;
import ro.gagarin.exceptions.AlreadyExistsException;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class ApplicationInitializer {

	private static final transient Logger LOG = Logger.getLogger(ApplicationInitializer.class);

	private static boolean initRun = false;

	public static boolean init() {

		synchronized (ApplicationInitializer.class) {
			if (initRun)
				return true;
			initRun = true;
		}

		LOG.info("Application initializer started");

		RoleManager roleManager = ModelFactory.getRoleManager();
		ConfigurationManager cfgManager = ModelFactory.getConfigurationManager();
		UserManager userManager = ModelFactory.getUserManager();

		String adminRoleName = cfgManager.getString(Config.ADMIN_ROLE_NAME);

		if (!checkCreatePermissionList(roleManager)) {
			LOG.error("Permission list verification failed; aborting init");
			return false;
		}

		UserRole adminRole = checkCreateAdminRole(roleManager, adminRoleName);
		checkAdminUsers(userManager, cfgManager, adminRole);
		checkAdminRolePermissionList(adminRole, roleManager);

		roleManager.release();

		LOG.info("Application initializer finished");
		return true;
	}

	private static UserRole checkCreateAdminRole(RoleManager roleManager, String adminRoleName) {
		LOG.info("Checking admin role existence");
		UserRole adminRole = roleManager.getRoleByName(adminRoleName);
		if (adminRole == null) {
			LOG.info("No admin role was found, creating role with " + adminRoleName);
			adminRole = new UserRole();
			adminRole.setRoleName(adminRoleName);
			try {
				roleManager.createRole(adminRole);
				LOG.info("Admin role created.");
			} catch (AlreadyExistsException e) {
				LOG.error("The role was not found but later exception was thrown.", e);
				return null;

			}
		}
		return adminRole;
	}

	private static boolean checkAdminUsers(UserManager userManager,
			ConfigurationManager cfgManager, UserRole adminRole) {
		LOG.info("Checking admin user");
		String adminUserName = cfgManager.getString(Config.ADMIN_USER_NAME);
		String adminPassword = cfgManager.getString(Config.ADMIN_PASSWORD);
		List<User> adminUsers = userManager.getUsersWithRole(adminRole);
		if (adminUsers == null || adminUsers.size() == 0) {
			LOG.info("admin user was not found; creating");
			User adminUser = null;
			adminUser = new User();
			adminUser.setRole(adminRole);
			adminUser.setUsername(adminUserName);
			adminUser.setPassword(adminPassword);
			adminUser.setName("Gagarin");
			try {
				userManager.createUser(adminUser);
			} catch (Exception e) {
				LOG.error("Exception while creating the admin user; aborting init", e);
				return false;
			}
			if (adminUsers == null)
				adminUsers = new ArrayList<User>(1);
			adminUsers.add(adminUser);
		}
		return true;
	}

	private static boolean checkAdminRolePermissionList(UserRole adminRole, RoleManager roleManager) {
		LOG.info("Checking AdminRolePermissionList to include all permissions");
		Set<UserPermission> grantedPermissions = adminRole.getUserPermissions();
		Set<UserPermission> newPermissions = new HashSet<UserPermission>();
		if (grantedPermissions == null) {
			grantedPermissions = new HashSet<UserPermission>();
			adminRole.setUserPermissions(grantedPermissions);
		}
		List<UserPermission> permissions = roleManager.getAllPermissions();
		if (permissions == null || permissions.size() == 0) {
			return false;
		}
		for (UserPermission userPermission : permissions) {
			if (!grantedPermissions.contains(userPermission)) {
				LOG.info("Adding permission " + userPermission.getPermissionName()
						+ " to admin role");
				newPermissions.add(userPermission);
				grantedPermissions.add(userPermission);
			} else {
				LOG.info("Already assigned " + userPermission.getPermissionName()
						+ " to admin role");
			}
		}
		try {
			roleManager.saveRole(adminRole);
		} catch (AlreadyExistsException e) {
			LOG.error("Saving the role failed", e);
			return false;
		}
		return true;
	}

	private static boolean checkCreatePermissionList(RoleManager roleManager) {
		LOG.info("Checking Permission List");
		PermissionEnum[] values = PermissionEnum.values();
		for (PermissionEnum permission : values) {
			UserPermission perm = new UserPermission();
			perm.setPermissionName(permission.name());
			try {
				roleManager.createPermission(perm);
			} catch (AlreadyExistsException e) {
				LOG.info("Permission " + permission.name() + " exists");
			}
		}
		return true;
	}
}
