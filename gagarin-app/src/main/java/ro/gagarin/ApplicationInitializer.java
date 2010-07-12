package ro.gagarin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.gagarin.application.objects.AppGroup;
import ro.gagarin.application.objects.AppUser;
import ro.gagarin.application.objects.AppUserPermission;
import ro.gagarin.application.objects.AppUserRole;
import ro.gagarin.config.Config;
import ro.gagarin.config.DBConfigManager;
import ro.gagarin.config.FileConfigurationManager;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.manager.BaseManager;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.AuthenticationType;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.user.UserStatus;
import ro.gagarin.utils.ConversionUtils;

// TODO:(2) use applog when ppropiate
public class ApplicationInitializer {

    private static final transient Logger LOG = Logger.getLogger(ApplicationInitializer.class);

    private static boolean initRun = false;

    private ConfigurationManager cfgManager;

    private UserDAO userDAO;

    private RoleDAO roleDAO;

    private String task;

    private Session session = null;

    private final BasicManagerFactory factory;

    private String adminRoleName;

    private UserRole adminRole;

    private User adminUser;

    private Group adminGroup;

    private String adminGroupName;

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
	this.adminRoleName = cfgManager.getString(Config.ADMIN_ROLE_NAME);

	this.setTask("CHK_CREATE_PERMISSIONLIST");
	checkCreatePermissionList();

	this.setTask("CHK_CREATE_ADMIN_ROLE");
	this.adminRole = checkCreateAdminRole();

	this.setTask("CHK_CREATE_ADMIN_ROLE_PERMISSION_LIST");
	checkAdminRolePermissionList();

	this.setTask("CHK_CREATE_ADMIN_USERS");
	checkAdminUser();

	this.setTask("CHK_CREATE_ADMIN_GROUP");
	checkAdminGroup();

	this.setTask("CHK_ASSIGN_ADMIN_ADMIN_GROUP");
	checkAssignAdminAdminGroup();

	this.setTask("CHK_ASSIGN_ADMIN_ADMIN_ROLE");
	checkAssignAdminAdminRole();

	this.setTask("CHANGE_CFG_MANAGER");
	changeConfigManager();

	this.setTask("DONE");

    }

    private void checkAssignAdminAdminRole() throws OperationException, DataConstraintException, ItemNotFoundException {
	Set<UserPermission> perms = roleDAO.getEffectivePermissionsOnEntity(BaseControlEntity.getAdminEntity(),
		adminGroup);
	if (perms.size() < PermissionEnum.values().length) {
	    LOG.info("There are missing permissions for admin group on Admin Control entity;"
		    + " assigning again admin ROLE");
	    roleDAO.assignRoleToOwner(adminRole, adminGroup, BaseControlEntity.getAdminEntity());
	}

    }

    private void checkAssignAdminAdminGroup() throws ItemNotFoundException, OperationException, DataConstraintException {
	List<Group> userGroups = userDAO.getUserGroups(adminUser);
	this.adminGroupName = cfgManager.getString(Config.ADMIN_GROUP_NAME);
	for (Group group : userGroups) {
	    if (adminGroupName.equals(group.getName())) {
		// the admin user belongs already to the admin group
		return;
	    }
	}

	LOG.info("admin user does not belongs to admin group; assigning...");
	userDAO.assignUserToGroup(adminUser, adminGroup);
    }

    private void checkAdminGroup() throws OperationException, DataConstraintException {
	String adminGroupName = cfgManager.getString(Config.ADMIN_GROUP_NAME);
	Group groupByName = userDAO.getGroupByName(adminGroupName);
	if (groupByName == null) {
	    LOG.info("admin group was not found; creating...");
	    AppGroup group = new AppGroup();
	    group.setName(adminGroupName);
	    group.setDescription("Autogenerated group for system administrators");
	    group.setId(userDAO.createGroup(group));
	    this.adminGroup = group;
	} else {
	    this.adminGroup = groupByName;
	}
    }

    private void changeConfigManager() throws InterruptedException {
	DBConfigManager dbConfigManager = DBConfigManager.getInstance();
	dbConfigManager.initializeManager();
	session.getManagerFactory().setConfigurationManager(dbConfigManager);
	LOG.info("Waiting DB Import");
	// TODO:(3) would be nice to have the DB import done at this point but
	// our session is not committed and the import job is running on a
	// different session. This causes dead lock especially with empty DB
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
	managers.add(factory.getAuthenticationManager());
	managers.add(factory.getAuthorizationManager());
	managers.add(factory.getSessionManager());
	managers.add(factory.getScheduleManager());

	for (BaseManager manager : managers) {
	    manager.initializeManager();
	}

	this.cfgManager = factory.getConfigurationManager();
	this.userDAO = factory.getDAOManager().getUserDAO(session);
	this.roleDAO = factory.getDAOManager().getRoleDAO(session);
    }

    private void checkCreateDBTables() throws OperationException {
	this.userDAO.checkCreateDependencies(this.cfgManager);
    }

    private UserRole checkCreateAdminRole() throws DataConstraintException, OperationException {
	LOG.info("Checking admin role existence");
	UserRole adminRole = roleDAO.getRoleByName(adminRoleName);
	if (adminRole == null) {
	    LOG.info("No admin role was found, creating role with " + adminRoleName);
	    AppUserRole aRole = new AppUserRole();
	    aRole.setRoleName(adminRoleName);
	    aRole.setId(roleDAO.createRole(aRole));
	    adminRole = aRole;
	    LOG.info("Admin role created.");
	}
	return adminRole;
    }

    private void checkAdminUser() throws ItemNotFoundException, DataConstraintException, OperationException {
	LOG.info("Checking admin user");
	final String adminUserName = cfgManager.getString(Config.ADMIN_USER_NAME);
	final String adminPassword = cfgManager.getString(Config.ADMIN_PASSWORD);
	User admin = userDAO.getUserByUsername(adminUserName);
	if (admin == null) {
	    LOG.info("admin user was not found; creating...");
	    AppUser adminUser = new AppUser();
	    adminUser.setName("Gagarin");
	    adminUser.setPassword(adminPassword);
	    adminUser.setUsername(adminUserName);
	    adminUser.setAuthentication(AuthenticationType.INTERNAL);
	    adminUser.setStatus(UserStatus.ACTIVE);
	    adminUser.setId(userDAO.createUser(adminUser));
	    this.adminUser = adminUser;
	} else {
	    this.adminUser = admin;
	}
    }

    private void checkAdminRolePermissionList() throws ItemNotFoundException, OperationException {
	LOG.info("Checking AdminRolePermissionList to include all permissions");
	Set<UserPermission> grantedPermissions = roleDAO.getRolePermissions(adminRole);
	if (grantedPermissions == null) {
	    grantedPermissions = new HashSet<UserPermission>();
	}

	HashSet<String> permSet = ConversionUtils.convertPermissionsToStringSet(grantedPermissions);

	List<UserPermission> allDBPermissions = roleDAO.getAllPermissions();

	for (UserPermission userPermission : allDBPermissions) {
	    if (!permSet.contains(userPermission.getPermissionName())) {
		LOG.info("Adding permission " + userPermission.getPermissionName() + " to admin role");
		roleDAO.assignPermissionToRole(adminRole, userPermission);
	    } else {
		LOG.info("Already assigned " + userPermission.getPermissionName() + " to admin role");
	    }
	}
    }

    private void checkCreatePermissionList() throws DataConstraintException, OperationException {
	LOG.info("Checking Permission List");
	PermissionEnum[] values = PermissionEnum.values();
	for (PermissionEnum permission : values) {

	    if (roleDAO.getPermissionByName(permission.name()) == null) {
		AppUserPermission perm = new AppUserPermission();
		perm.setPermissionName(permission.name());
		roleDAO.createPermission(perm);
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
