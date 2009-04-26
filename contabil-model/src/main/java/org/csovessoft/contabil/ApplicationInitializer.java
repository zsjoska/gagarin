package org.csovessoft.contabil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.csovessoft.contabil.config.Config;
import org.csovessoft.contabil.exceptions.AlreadyExistsException;
import org.csovessoft.contabil.user.PermissionEnum;
import org.csovessoft.contabil.user.User;
import org.csovessoft.contabil.user.UserPermission;
import org.csovessoft.contabil.user.UserRole;

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
		ConfigurationManager configurationManager = ModelFactory.getConfigurationManager();
		UserManager userManager = ModelFactory.getUserManager();

		String adminRoleName = configurationManager.getString(Config.ADMIN_ROLE_NAME);
		String adminUserName = configurationManager.getString(Config.ADMIN_USER_NAME);
		String adminPassword = configurationManager.getString(Config.ADMIN_PASSWORD);

		LOG.info("Checking Permission List");

		if (!checkCreatePermissionList(roleManager)) {
			LOG.error("Permission list verification failed; aborting init");
			return false;
		}

		LOG.info("Checking admin role existence");
		UserRole adminRole = roleManager.getAdminRole(adminRoleName);
		if (adminRole == null) {
			LOG.info("No admin role was found, creating role with " + adminRoleName);
			adminRole = new UserRole();
			adminRole.setRoleName(adminRoleName);
			try {
				roleManager.createRole(adminRole);
				LOG.info("Admin role created.");
			} catch (AlreadyExistsException e) {
				LOG.error("The role was not found but later exception was thrown.", e);
				return false;

			}
		}

		LOG.info("Checking admin user");

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

		checkAdminRolePermissionList(adminRole, roleManager);

		LOG.info("Application initializer finished");
		return true;
	}

	private static boolean checkAdminRolePermissionList(UserRole adminRole, RoleManager roleManager) {
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
