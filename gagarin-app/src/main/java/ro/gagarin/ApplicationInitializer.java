package ro.gagarin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.gagarin.application.objects.AppUser;
import ro.gagarin.application.objects.AppUserPermission;
import ro.gagarin.application.objects.AppUserRole;
import ro.gagarin.config.Config;
import ro.gagarin.config.DBConfigManager;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class ApplicationInitializer {

	private static final transient Logger LOG = Logger.getLogger(ApplicationInitializer.class);

	private static boolean initRun = false;

	private ConfigurationManager cfgManager;

	private UserDAO userManager;

	private RoleDAO roleManager;

	private String task;

	private final Session session;

	public ApplicationInitializer(Session session) {
		this.session = session;
	}

	public static synchronized boolean init() throws OperationException {

		if (initRun)
			return false;
		initRun = true;

		LOG
				.info("---------------------------- Application initializer started ------------------------");

		Session session = prepareInitSession();

		ApplicationInitializer initializer = new ApplicationInitializer(session);
		try {

			initializer.doInit();
		} catch (Exception e) {
			LOG.error("Application initializer failed for task:" + initializer.getTask(), e);
			throw new OperationException(ErrorCodes.STARTUP_FAILED, e);
		} finally {
			initializer.session.getManagerFactory().releaseSession(session);
		}

		LOG
				.info("---------------------------- Application initializer finished ----------------------------");
		return true;
	}

	private static Session prepareInitSession() {
		Session session = new Session();
		session.setManagerFactory(BasicManagerFactory.getInstance());
		session.setBusy(true, new Throwable("Session leak notice"));
		AppUser user = new AppUser();
		user.setUsername("SYSINIT");
		session.setUser(user);
		return session;
	}

	private void doInit() throws ItemNotFoundException, OperationException, DataConstraintException {

		this.setTask("CREATE_MANAGERS");
		initManagers(this.session);

		this.setTask("CHECK_DB_TABLES");
		checkCreateDBTables();

		this.setTask("GET_CFG_ADMIN_ROLE");
		String adminRoleName = cfgManager.getString(Config.ADMIN_ROLE_NAME);

		this.setTask("CHK_CREATE_PERMISSIONLIST");
		checkCreatePermissionList();

		this.setTask("CHK_CREATE_ADMIN_ROLE");
		UserRole adminRole = checkCreateAdminRole(adminRoleName);

		this.setTask("CHK_CREATE_ADMIN_ROLE_PERMISSION_LIST");
		checkAdminRolePermissionList(adminRole);

		this.setTask("CHK_CREATE_ADMIN_USERS");
		checkAdminUsers(adminRole);

		this.setTask("CHANGE_CFG_MANAGER");
		session.getManagerFactory().setConfigurationManager(DBConfigManager.getInstance());

		this.setTask("DONE");

	}

	private void initManagers(Session session) throws OperationException {
		ManagerFactory factory = session.getManagerFactory();
		this.cfgManager = factory.getConfigurationManager(session);
		this.userManager = factory.getDAOManager().getUserDAO(session);
		this.roleManager = factory.getDAOManager().getRoleDAO(session);
	}

	private void checkCreateDBTables() throws OperationException {
		this.userManager.checkCreateDependencies(this.cfgManager);

	}

	private UserRole checkCreateAdminRole(final String adminRoleName)
			throws DataConstraintException, OperationException {
		LOG.info("Checking admin role existence");
		UserRole adminRole = roleManager.getRoleByName(adminRoleName);
		if (adminRole == null) {
			LOG.info("No admin role was found, creating role with " + adminRoleName);
			AppUserRole aRole = new AppUserRole();
			aRole.setRoleName(adminRoleName);
			adminRole = aRole;
			roleManager.createRole(adminRole);
			LOG.info("Admin role created.");
		}
		return adminRole;
	}

	private void checkAdminUsers(final UserRole adminRole) throws ItemNotFoundException,
			DataConstraintException, OperationException {
		LOG.info("Checking admin user");
		final String adminUserName = cfgManager.getString(Config.ADMIN_USER_NAME);
		final String adminPassword = cfgManager.getString(Config.ADMIN_PASSWORD);
		List<User> adminUsers = userManager.getUsersWithRole(adminRole);
		if (adminUsers == null || adminUsers.size() == 0) {
			LOG.info("admin user was not found; creating");
			AppUser adminUser = new AppUser();
			adminUser.setName("Gagarin");
			adminUser.setPassword(adminPassword);
			adminUser.setUsername(adminUserName);
			adminUser.setRole(adminRole);
			userManager.createUser(adminUser);
			if (adminUsers == null)
				adminUsers = new ArrayList<User>(1);
			adminUsers.add(adminUser);
		}
	}

	private void checkAdminRolePermissionList(UserRole adminRole) throws ItemNotFoundException,
			OperationException {
		LOG.info("Checking AdminRolePermissionList to include all permissions");
		Set<UserPermission> grantedPermissions = roleManager.getRolePermissions(adminRole);
		if (grantedPermissions == null) {
			grantedPermissions = new HashSet<UserPermission>();
		}

		List<UserPermission> permissions = roleManager.getAllPermissions();
		if (permissions == null || permissions.size() == 0) {
			throw new RuntimeException(
					"Inconsistent state: The permission list should have been created!");
		}

		for (UserPermission userPermission : permissions) {
			if (!grantedPermissions.contains(userPermission)) {
				LOG.info("Adding permission " + userPermission.getPermissionName()
						+ " to admin role");
				roleManager.assignPermissionToRole(adminRole, userPermission);
			} else {
				LOG.info("Already assigned " + userPermission.getPermissionName()
						+ " to admin role");
			}
		}
	}

	private void checkCreatePermissionList() throws DataConstraintException, OperationException {
		LOG.info("Checking Permission List");
		PermissionEnum[] values = PermissionEnum.values();
		for (PermissionEnum permission : values) {

			if (roleManager.getPermissionByName(permission.name()) == null) {
				AppUserPermission perm = new AppUserPermission();
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
