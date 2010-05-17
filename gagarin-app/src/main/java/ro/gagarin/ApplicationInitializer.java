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
import ro.gagarin.config.FileConfigurationManager;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;
import ro.gagarin.user.AuthenticationType;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.user.UserStatus;
import ro.gagarin.utils.ConversionUtils;

public class ApplicationInitializer {

    private static final transient Logger LOG = Logger.getLogger(ApplicationInitializer.class);

    private static boolean initRun = false;

    private ConfigurationManager cfgManager;

    private UserDAO userManager;

    private RoleDAO roleManager;

    private String task;

    private Session session = null;

    private final BasicManagerFactory factory;

    public ApplicationInitializer(BasicManagerFactory instance) {
	this.factory = instance;

    }

    public static synchronized boolean init(BasicManagerFactory instance) throws OperationException {

	if (initRun)
	    return false;
	initRun = true;

	LOG.info("---------------------------- Application initializer started ------------------------");

	ApplicationInitializer initializer = new ApplicationInitializer(instance);
	try {
	    initializer.doInit();
	} catch (Exception e) {
	    LOG.error("Application initializer failed for task:" + initializer.getTask(), e);
	    throw new OperationException(ErrorCodes.STARTUP_FAILED, e);
	} finally {
	    if (initializer.session != null) {
		initializer.session.getManagerFactory().releaseSession(initializer.session);
	    }
	}

	LOG.info("\n\n---------------------------- Application initializer finished ----------------------------\n");
	return true;
    }

    private Session prepareInitSession() {
	Session session = new Session();
	session.setManagerFactory(this.factory);
	session.setBusy(true, new Throwable("Session leak notice"));
	session.setReason("SYSINIT");
	session.setSessionString(System.currentTimeMillis() + "-" + System.nanoTime());

	AppUser user = new AppUser();
	user.setUsername("SYSINIT");
	session.setUser(user);
	return session;
    }

    private void doInit() throws ItemNotFoundException, OperationException, DataConstraintException,
	    InterruptedException {

	this.setTask("LOAD_FILE_CONFIGURATION");
	loadFileConfiguration();

	this.setTask("PREPARE_INIT_SESSION");
	this.session = prepareInitSession();

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
	changeConfigManager();

	this.setTask("DONE");

    }

    private void changeConfigManager() throws InterruptedException {
	DBConfigManager dbConfigManager = DBConfigManager.getInstance();
	dbConfigManager.initializeManager();
	session.getManagerFactory().setConfigurationManager(dbConfigManager);
	LOG.info("Waiting DB Import");
	// TODO: would be nice to have the DB import done at this point but our
	// session is not committed and the import job is running on a different
	// session. This causes dead lock especially with empty DB
	// dbConfigManager.waitForDBImport();

    }

    private void loadFileConfiguration() {
	this.cfgManager = FileConfigurationManager.getInstance();
    }

    private void initManagers(Session session) throws OperationException {
	ArrayList<BaseManager> managers = new ArrayList<BaseManager>();

	factory.initializeManagers();

	managers.add(factory.getConfigurationManager());
	managers.add(factory.getDAOManager());
	managers.add(factory.getAuthenticationManager(session));
	managers.add(factory.getAuthorizationManager());
	managers.add(factory.getSessionManager());
	managers.add(factory.getScheduleManager());

	for (BaseManager manager : managers) {
	    manager.initializeManager();
	}

	this.cfgManager = factory.getConfigurationManager();
	this.userManager = factory.getDAOManager().getUserDAO(session);
	this.roleManager = factory.getDAOManager().getRoleDAO(session);
    }

    private void checkCreateDBTables() throws OperationException {
	this.userManager.checkCreateDependencies(this.cfgManager);
    }

    private UserRole checkCreateAdminRole(final String adminRoleName) throws DataConstraintException,
	    OperationException {
	LOG.info("Checking admin role existence");
	UserRole adminRole = roleManager.getRoleByName(adminRoleName);
	if (adminRole == null) {
	    LOG.info("No admin role was found, creating role with " + adminRoleName);
	    AppUserRole aRole = new AppUserRole();
	    aRole.setRoleName(adminRoleName);
	    aRole.setId(roleManager.createRole(aRole));
	    adminRole = aRole;
	    LOG.info("Admin role created.");
	}
	return adminRole;
    }

    private void checkAdminUsers(final UserRole adminRole) throws ItemNotFoundException, DataConstraintException,
	    OperationException {
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
	    adminUser.setAuthentication(AuthenticationType.INTERNAL);
	    adminUser.setStatus(UserStatus.ACTIVE);
	    userManager.createUser(adminUser);
	    if (adminUsers == null)
		adminUsers = new ArrayList<User>(1);
	    adminUsers.add(adminUser);
	}
    }

    private void checkAdminRolePermissionList(UserRole adminRole) throws ItemNotFoundException, OperationException {
	LOG.info("Checking AdminRolePermissionList to include all permissions");
	Set<UserPermission> grantedPermissions = roleManager.getRolePermissions(adminRole);
	if (grantedPermissions == null) {
	    grantedPermissions = new HashSet<UserPermission>();
	}

	HashSet<String> permSet = ConversionUtils.convertPermissionsToStringSet(grantedPermissions);

	List<UserPermission> allDBPermissions = roleManager.getAllPermissions();

	for (UserPermission userPermission : allDBPermissions) {
	    if (!permSet.contains(userPermission.getPermissionName())) {
		LOG.info("Adding permission " + userPermission.getPermissionName() + " to admin role");
		roleManager.assignPermissionToRole(adminRole, userPermission);
	    } else {
		LOG.info("Already assigned " + userPermission.getPermissionName() + " to admin role");
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
	LOG.info(" ######## Executing task " + task);
    }

    public String getTask() {
	return task;
    }
}
