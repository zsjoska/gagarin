package ro.gagarin.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import ro.gagarin.user.Group;
import ro.gagarin.ws.executor.WSException;
import ro.gagarin.ws.executor.WebserviceExecutor;
import ro.gagarin.ws.objects.WSConfig;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.objects.WSStatistic;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;
import ro.gagarin.ws.userservice.CreateGroupOP;
import ro.gagarin.ws.userservice.CreateRoleWithPermissionsOP;
import ro.gagarin.ws.userservice.CreateUserOP;
import ro.gagarin.ws.userservice.DeleteRoleOP;
import ro.gagarin.ws.userservice.GetAllPermissionListOP;
import ro.gagarin.ws.userservice.GetConfigEntriesOP;
import ro.gagarin.ws.userservice.GetGroupsOP;
import ro.gagarin.ws.userservice.GetLogEntriesOP;
import ro.gagarin.ws.userservice.GetRoleListOP;
import ro.gagarin.ws.userservice.GetRolePermissionstOP;
import ro.gagarin.ws.userservice.GetSessionListOP;
import ro.gagarin.ws.userservice.GetStatisticsOP;
import ro.gagarin.ws.userservice.GetUsersOP;
import ro.gagarin.ws.userservice.LogoutSessionOP;
import ro.gagarin.ws.userservice.SetConfigEntryOP;

// TODO: rename to AdminService
@WebService
public class UserService {

    @WebMethod
    public Long createUser(String sessionId, WSUser user) throws WSException {

	CreateUserOP createUser = new CreateUserOP(sessionId, user);
	WebserviceExecutor.execute(createUser);
	return createUser.getUserId();

    }

    @WebMethod
    public List<WSUserRole> getRoleList(String sessionId) throws WSException {

	GetRoleListOP getRoleList = new GetRoleListOP(sessionId);
	WebserviceExecutor.execute(getRoleList);
	return getRoleList.getRoleList();

    }

    @WebMethod
    public WSUserRole createRoleWithPermissions(String sessionId, String roleName, WSUserPermission[] permissions)
	    throws WSException {

	CreateRoleWithPermissionsOP createRoleWithPermissions = new CreateRoleWithPermissionsOP(sessionId, roleName,
		permissions);
	WebserviceExecutor.execute(createRoleWithPermissions);
	return createRoleWithPermissions.getRole();

    }

    @WebMethod
    public List<WSUserPermission> getAllPermissionList(String sessionId) throws WSException {

	GetAllPermissionListOP getAllPermissionList = new GetAllPermissionListOP(sessionId);
	WebserviceExecutor.execute(getAllPermissionList);
	return getAllPermissionList.getPermissionList();
    }

    @WebMethod
    public List<WSUserPermission> getRolePermissions(String sessionId, WSUserRole wsUserRole) throws WSException {

	GetRolePermissionstOP getRolePermissions = new GetRolePermissionstOP(sessionId, wsUserRole);
	WebserviceExecutor.execute(getRolePermissions);
	return getRolePermissions.getRolePermissions();

    }

    @WebMethod
    public void deleteRole(String sessionId, WSUserRole wsUserRole) throws WSException {

	DeleteRoleOP deleteRole = new DeleteRoleOP(sessionId, wsUserRole);
	WebserviceExecutor.execute(deleteRole);

    }

    @WebMethod
    public List<WSUser> getUsers(String sessionId) throws WSException {

	GetUsersOP getUsers = new GetUsersOP(sessionId);
	WebserviceExecutor.execute(getUsers);
	return getUsers.getUsers();

    }

    @WebMethod
    public List<WSConfig> getConfigEntries(String sessionId) throws WSException {

	GetConfigEntriesOP getConfigEntries = new GetConfigEntriesOP(sessionId);
	WebserviceExecutor.execute(getConfigEntries);
	return getConfigEntries.getConfigEntries();

    }

    @WebMethod
    public void setConfigEntry(String sessionId, WSConfig wsConfig) throws WSException {

	SetConfigEntryOP setConfigEntry = new SetConfigEntryOP(sessionId, wsConfig);
	WebserviceExecutor.execute(setConfigEntry);

    }

    @WebMethod
    public List<WSLogEntry> getLogEntries(String sessionId, String user) throws WSException {

	GetLogEntriesOP getLogEntries = new GetLogEntriesOP(sessionId, user);
	WebserviceExecutor.execute(getLogEntries);
	return getLogEntries.getLogEntries();
    }

    @WebMethod
    public List<WSExportedSession> getSessionList(String sessionId) throws WSException {

	GetSessionListOP getSessionList = new GetSessionListOP(sessionId);
	WebserviceExecutor.execute(getSessionList);
	return getSessionList.getSessionList();

    }

    @WebMethod
    public void logoutSession(String sessionId, String otherSessionId) throws WSException {

	WebserviceExecutor.execute(new LogoutSessionOP(sessionId, otherSessionId));

    }

    @WebMethod
    public List<WSStatistic> getStatistics(String sessionId, String filter) throws WSException {

	GetStatisticsOP getStatistics = new GetStatisticsOP(sessionId, filter);
	WebserviceExecutor.execute(getStatistics);
	return getStatistics.getStatisticList();

    }

    @WebMethod
    public Long createGroup(String sessionId, WSGroup group) throws WSException {
	CreateGroupOP createGroup = new CreateGroupOP(sessionId, group);
	WebserviceExecutor.execute(createGroup);
	return createGroup.getGroupId();
    }

    // FIXME: Group -> WSGroup
    @WebMethod
    public List<Group> getGroups(String sessionId) throws WSException {
	GetGroupsOP getGroups = new GetGroupsOP(sessionId);
	WebserviceExecutor.execute(getGroups);
	return getGroups.getGroups();
    }

    @WebMethod
    public void deleteGroup(String sessionId, WSGroup group) throws WSException {
	// DeleteGroupOP deleteGroup = new DeleteGroupOP(sessionId, group);
	// WebserviceExecutor.execute(deleteGroup);
    }

    @WebMethod
    public void updateGroup(String sessionId, WSGroup group) throws WSException {
	// UpdateGroupOP updateGroup = new UpdateGroupOP(sessionId, group);
	// WebserviceExecutor.execute(updateGroup);
    }

    @WebMethod
    public List<WSGroup> getUserGroups(String sessionId, WSUser user) throws WSException {
	// GetUserGroupsOP getUserGroups = new GetUserGroupsOP(sessionId);
	// WebserviceExecutor.execute(getUserGroups);
	// return getUserGroups.getUserGroups();
	return null;
    }

    @WebMethod
    public List<WSUser> getGroupUsers(String sessionId, WSGroup group) throws WSException {
	// GetGroupUsersOP getGroupUsers = new GetGroupUsersOP(sessionId);
	// WebserviceExecutor.execute(getGroupUsers);
	// return getGroupUsers.getGroupUsers();
	return null;
    }

    @WebMethod
    public List<WSUser> assignUsersToGroup(String sessionId, WSGroup group, WSUser[] users) throws WSException {
	// GetGroupUsersOP getGroupUsers = new GetGroupUsersOP(sessionId);
	// WebserviceExecutor.execute(getGroupUsers);
	// return getGroupUsers.getGroupUsers();
	return null;
    }
}
