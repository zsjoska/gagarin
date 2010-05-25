package ro.gagarin.jdbc;

import static ro.gagarin.utils.ConversionUtils.group2String;
import static ro.gagarin.utils.ConversionUtils.user2String;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.group.AssignUserToGroupSQL;
import ro.gagarin.jdbc.group.CreateGroupSQL;
import ro.gagarin.jdbc.group.DeleteGroupSQL;
import ro.gagarin.jdbc.group.GetGroupUsersSQL;
import ro.gagarin.jdbc.group.GetUserGroupsSQL;
import ro.gagarin.jdbc.group.SelectGroupByNameSQL;
import ro.gagarin.jdbc.group.SelectGroupsSQL;
import ro.gagarin.jdbc.group.UnassignUserFromGroupSQL;
import ro.gagarin.jdbc.group.UpdateGroupSQL;
import ro.gagarin.jdbc.objects.DBGroup;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.jdbc.user.CreateUserSQL;
import ro.gagarin.jdbc.user.DeleteGroupAssignmentsSQL;
import ro.gagarin.jdbc.user.DeleteUserSQL;
import ro.gagarin.jdbc.user.SelectUserByUsernamePasswordSQL;
import ro.gagarin.jdbc.user.SelectUserByUsernameSQL;
import ro.gagarin.jdbc.user.SelectUsersSQL;
import ro.gagarin.jdbc.user.UpdateUserSQL;
import ro.gagarin.log.AppLog;
import ro.gagarin.log.AppLogAction;
import ro.gagarin.session.Session;
import ro.gagarin.user.AuthenticationType;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;

public class JdbcUserDAO extends BaseJdbcDAO implements UserDAO {

    public JdbcUserDAO(Session session) throws OperationException {
	super(session);
    }

    public Group completeGroupId(Group group) throws OperationException, ItemNotFoundException {
	Group gr = group;
	if (gr.getId() == null && gr.getName() != null) {
	    gr = SelectGroupByNameSQL.execute(this, gr.getName());
	    if (gr == null) {
		throw new ItemNotFoundException(Group.class, group.getName());
	    }
	}
	return gr;
    }

    public User completeUserId(User user) throws OperationException, ItemNotFoundException {
	User usr = user;
	if (usr.getId() == null && usr.getUsername() != null) {
	    usr = SelectUserByUsernameSQL.execute(this, usr.getUsername());
	    if (usr == null) {
		throw new ItemNotFoundException(User.class, user.getUsername());
	    }
	}
	return usr;
    }

    @Override
    public User userLogin(String username, String password) throws ItemNotFoundException, OperationException {

	try {

	    User user = SelectUserByUsernamePasswordSQL.execute(this, username, password);
	    if (user == null) {
		throw new ItemNotFoundException(User.class, username + " with password");
	    }

	    APPLOG.action(AppLogAction.LOGIN, User.class, username, AppLog.SUCCESS);
	    APPLOG.info("User " + username + " logged in");
	    return user;
	} catch (OperationException e) {
	    APPLOG.action(AppLogAction.LOGIN, User.class, username, AppLog.FAILED);
	    throw e;
	} catch (ItemNotFoundException e) {
	    APPLOG.action(AppLogAction.LOGIN, User.class, username, AppLog.FAILED);
	    throw e;
	}

    }

    @Override
    public long createUser(User user) throws DataConstraintException, OperationException, ItemNotFoundException {

	try {

	    DBUser dbUser = new DBUser(user);
	    dbUser.setId(DBUser.getNextId());
	    dbUser.setCreated(System.currentTimeMillis());

	    if (dbUser.getAuthentication() == null) {
		dbUser.setAuthentication(AuthenticationType.INTERNAL);
	    }

	    new CreateUserSQL(this, dbUser).execute();

	    APPLOG.action(AppLogAction.CREATE, User.class, user.getUsername(), AppLog.SUCCESS);
	    APPLOG.info("User " + user.getUsername() + " was created");
	    return dbUser.getId();
	} catch (OperationException e) {
	    APPLOG.error("Could not create user:" + user2String(user), e);
	    APPLOG.action(AppLogAction.CREATE, User.class, user.getUsername(), AppLog.FAILED);
	    throw e;
	}
    }

    @Override
    public User getUserByUsername(String username) throws OperationException {

	User user = SelectUserByUsernameSQL.execute(this, username);
	return user;
    }

    @Override
    public void deleteUser(User user) throws OperationException, DataConstraintException, ItemNotFoundException {

	User usr = completeUserId(user);
	try {
	    new DeleteUserSQL(this, usr).execute();
	    APPLOG.action(AppLogAction.DELETE, User.class, usr.getUsername(), AppLog.SUCCESS);
	    APPLOG.info("User " + usr.getUsername() + " was deleted");
	} catch (OperationException e) {
	    APPLOG.error("Could not delete user:" + user2String(usr), e);
	    APPLOG.action(AppLogAction.DELETE, User.class, usr.getUsername(), AppLog.FAILED);
	    throw e;
	}
    }

    @Override
    public List<User> getAllUsers() throws OperationException {
	ArrayList<User> users = SelectUsersSQL.execute(this);
	return users;
    }

    @Override
    public Long createGroup(Group group) throws DataConstraintException, OperationException {
	try {

	    DBGroup dbGroup = new DBGroup(group);
	    dbGroup.setId(DBUser.getNextId());

	    new CreateGroupSQL(this, dbGroup).execute();

	    APPLOG.action(AppLogAction.CREATE, Group.class, group.getName(), AppLog.SUCCESS);
	    APPLOG.info("Group " + group.getName() + " was created");
	    return dbGroup.getId();
	} catch (OperationException e) {
	    APPLOG.error("Could not create user:" + group2String(group), e);
	    APPLOG.action(AppLogAction.CREATE, Group.class, group.getName(), AppLog.FAILED);
	    throw e;
	}
    }

    @Override
    public Group getGroupByName(String groupname) throws OperationException {
	Group group = SelectGroupByNameSQL.execute(this, groupname);
	return group;
    }

    @Override
    public List<Group> getGroups() throws OperationException {
	return SelectGroupsSQL.execute(this);
    }

    @Override
    public void deleteGroup(Group group) throws OperationException, DataConstraintException, ItemNotFoundException {

	Group gr = completeGroupId(group);
	new DeleteGroupSQL(this, gr).execute();
    }

    @Override
    public void updateGroup(Group group) throws OperationException, DataConstraintException, ItemNotFoundException {
	int recCount = new UpdateGroupSQL(this, group).execute();
	if (recCount == 0) {
	    throw new ItemNotFoundException(Group.class, group.getId().toString());
	}
	if (recCount != 1) {
	    throw new OperationException(ErrorCodes.INTERNAL_ERROR, recCount + " rows changed for group ID"
		    + group.getId());
	}
    }

    @Override
    public void updateUser(User user) throws OperationException, DataConstraintException, ItemNotFoundException {
	int recCount = new UpdateUserSQL(this, user).execute();
	if (recCount == 0) {
	    throw new ItemNotFoundException(Group.class, user.getId().toString());
	}
	if (recCount != 1) {
	    throw new OperationException(ErrorCodes.INTERNAL_ERROR, recCount + " rows changed for group ID"
		    + user.getId());
	}
    }

    @Override
    public void assignUserToGroup(User user, Group group) throws OperationException, ItemNotFoundException,
	    DataConstraintException {
	Group gr = completeGroupId(group);
	User usr = completeUserId(user);

	// TODO:(2) add check for group id and user id existence

	new AssignUserToGroupSQL(this, usr, gr).execute();
    }

    @Override
    public List<User> getGroupUsers(Group group) throws OperationException, ItemNotFoundException {

	Group gr = completeGroupId(group);
	return GetGroupUsersSQL.execute(this, gr);
    }

    @Override
    public List<Group> getUserGroups(User user) throws ItemNotFoundException, OperationException {

	User usr = completeUserId(user);
	return GetUserGroupsSQL.execute(this, usr);
    }

    @Override
    public void unassignUserFromGroup(User user, Group group) throws OperationException, ItemNotFoundException,
	    DataConstraintException {
	Group gr = completeGroupId(group);
	User usr = completeUserId(user);

	// TODO:(2) add check for group id and user id existence

	new UnassignUserFromGroupSQL(this, usr, gr).execute();
    }

    @Override
    public void deleteGroupAssignments(Group group) throws OperationException, DataConstraintException {
	new DeleteGroupAssignmentsSQL(this, group).execute();
    }
}
