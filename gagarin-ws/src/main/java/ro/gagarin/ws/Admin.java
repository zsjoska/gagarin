package ro.gagarin.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import ro.gagarin.ControlEntityCategory;
import ro.gagarin.ws.executor.WSException;
import ro.gagarin.ws.executor.WebserviceExecutor;
import ro.gagarin.ws.objects.WSConfig;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.objects.WSPermPersonCEAssignment;
import ro.gagarin.ws.objects.WSPerson;
import ro.gagarin.ws.objects.WSStatistic;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;
import ro.gagarin.ws.userservice.AssignRoleToControlEntityOP;
import ro.gagarin.ws.userservice.AssignUsersToGroupOP;
import ro.gagarin.ws.userservice.CreateGroupOP;
import ro.gagarin.ws.userservice.CreateRoleWithPermissionsOP;
import ro.gagarin.ws.userservice.CreateUserOP;
import ro.gagarin.ws.userservice.DeleteGroupOP;
import ro.gagarin.ws.userservice.DeleteRoleOP;
import ro.gagarin.ws.userservice.DeleteUserOP;
import ro.gagarin.ws.userservice.GetAllPermissionListOP;
import ro.gagarin.ws.userservice.GetConfigEntriesOP;
import ro.gagarin.ws.userservice.GetControlEntityCategoriesOP;
import ro.gagarin.ws.userservice.GetControlEntityListForCategoryOP;
import ro.gagarin.ws.userservice.GetGroupUsersOP;
import ro.gagarin.ws.userservice.GetGroupsOP;
import ro.gagarin.ws.userservice.GetLogEntriesOP;
import ro.gagarin.ws.userservice.GetPermissionAssignmentsForControlEntityOP;
import ro.gagarin.ws.userservice.GetPersonsOP;
import ro.gagarin.ws.userservice.GetRoleListOP;
import ro.gagarin.ws.userservice.GetRolePermissionsOP;
import ro.gagarin.ws.userservice.GetSessionListOP;
import ro.gagarin.ws.userservice.GetStatisticsOP;
import ro.gagarin.ws.userservice.GetUserGroupsOP;
import ro.gagarin.ws.userservice.GetUsersOP;
import ro.gagarin.ws.userservice.LogoutSessionOP;
import ro.gagarin.ws.userservice.SetConfigEntryOP;
import ro.gagarin.ws.userservice.UnAssignRoleFromControlEntityOP;
import ro.gagarin.ws.userservice.UnassignUsersFromGroupOP;
import ro.gagarin.ws.userservice.UpdateGroupOP;
import ro.gagarin.ws.userservice.UpdateRoleOP;
import ro.gagarin.ws.userservice.UpdateUserOP;

@WebService
public class Admin {

    @WebMethod
    public Long createUser(String sessionId, WSUser user) throws WSException {

	CreateUserOP createUser = new CreateUserOP(sessionId, user);
	WebserviceExecutor.execute(createUser);
	return createUser.getUserId();

    }

    @WebMethod
    public void deleteUser(String sessionId, WSUser user) throws WSException {
	DeleteUserOP deleteUser = new DeleteUserOP(sessionId, user);
	WebserviceExecutor.execute(deleteUser);
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

	GetRolePermissionsOP getRolePermissions = new GetRolePermissionsOP(sessionId, wsUserRole);
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

    @WebMethod
    public List<WSGroup> getGroups(String sessionId) throws WSException {
	GetGroupsOP getGroups = new GetGroupsOP(sessionId);
	WebserviceExecutor.execute(getGroups);
	return getGroups.getGroups();
    }

    @WebMethod
    public void deleteGroup(String sessionId, WSGroup group) throws WSException {
	DeleteGroupOP deleteGroup = new DeleteGroupOP(sessionId, group);
	WebserviceExecutor.execute(deleteGroup);
    }

    @WebMethod
    public void updateGroup(String sessionId, WSGroup group) throws WSException {
	UpdateGroupOP updateGroup = new UpdateGroupOP(sessionId, group);
	WebserviceExecutor.execute(updateGroup);
    }

    @WebMethod
    public void updateUser(String sessionId, WSUser user) throws WSException {
	UpdateUserOP updateUser = new UpdateUserOP(sessionId, user);
	WebserviceExecutor.execute(updateUser);
    }

    @WebMethod
    public void updateRole(String sessionId, WSUserRole role, WSUserPermission[] permissions) throws WSException {
	UpdateRoleOP updateRole = new UpdateRoleOP(sessionId, role, permissions);
	WebserviceExecutor.execute(updateRole);
    }

    @WebMethod
    public List<WSGroup> getUserGroups(String sessionId, WSUser user) throws WSException {
	GetUserGroupsOP getUserGroups = new GetUserGroupsOP(sessionId, user);
	WebserviceExecutor.execute(getUserGroups);
	return getUserGroups.getUserGroups();
    }

    @WebMethod
    public List<WSUser> getGroupUsers(String sessionId, WSGroup group) throws WSException {
	GetGroupUsersOP getGroupUsers = new GetGroupUsersOP(sessionId, group);
	WebserviceExecutor.execute(getGroupUsers);
	return getGroupUsers.getGroupUsers();
    }

    @WebMethod
    public void assignUsersToGroup(String sessionId, WSGroup group, WSUser[] users) throws WSException {
	AssignUsersToGroupOP assignUsersToGroup = new AssignUsersToGroupOP(sessionId, group, users);
	WebserviceExecutor.execute(assignUsersToGroup);
    }

    @WebMethod
    public void unassignUsersFromGroup(String sessionId, WSGroup group, WSUser[] users) throws WSException {
	UnassignUsersFromGroupOP unassignUsersFromGroup = new UnassignUsersFromGroupOP(sessionId, group, users);
	WebserviceExecutor.execute(unassignUsersFromGroup);
    }

    @WebMethod
    public List<ControlEntityCategory> getControlEntityCategories(String sessionId) throws WSException {
	GetControlEntityCategoriesOP getControlEntityCategories = new GetControlEntityCategoriesOP(sessionId);
	WebserviceExecutor.execute(getControlEntityCategories);
	return getControlEntityCategories.getControlEntities();
    }

    @WebMethod
    public List<WSControlEntity> getControlEntityListForCategory(String sessionId, String category) throws WSException {
	GetControlEntityListForCategoryOP getControlEntityListForCategory = new GetControlEntityListForCategoryOP(
		sessionId, category);
	WebserviceExecutor.execute(getControlEntityListForCategory);
	return getControlEntityListForCategory.getControlEntities();
    }

    @WebMethod
    public List<WSPermPersonCEAssignment> getPermissionAssignmentsForControlEntity(String sessionId, WSControlEntity ce)
	    throws WSException {
	GetPermissionAssignmentsForControlEntityOP op = new GetPermissionAssignmentsForControlEntityOP(sessionId, ce);
	WebserviceExecutor.execute(op);
	return op.getPermissionAssignments();
    }

    @WebMethod
    public List<WSPerson> getPersons(String sessionId) throws WSException {
	GetPersonsOP getPersons = new GetPersonsOP(sessionId);
	WebserviceExecutor.execute(getPersons);
	return getPersons.getPersons();
    }

    @WebMethod
    public void assignRoleToControlEntity(String sessionId, WSControlEntity ce, WSUserRole role, WSPerson person)
	    throws WSException {

	AssignRoleToControlEntityOP op = new AssignRoleToControlEntityOP(sessionId, ce, role, person);
	WebserviceExecutor.execute(op);
    }

    @WebMethod
    public void unAssignRoleFromControlEntity(String sessionId, WSControlEntity ce, WSUserRole role, WSPerson person)
	    throws WSException {

	UnAssignRoleFromControlEntityOP op = new UnAssignRoleFromControlEntityOP(sessionId, ce, role, person);
	WebserviceExecutor.execute(op);
    }
}
