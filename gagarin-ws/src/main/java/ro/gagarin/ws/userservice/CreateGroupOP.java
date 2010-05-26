package ro.gagarin.ws.userservice;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;

public class CreateGroupOP extends WebserviceOperation {

    private UserDAO userDAO;

    private final WSGroup group;

    private Long groupId = null;

    public CreateGroupOP(String sessionId, WSGroup group) {
	super(sessionId);
	this.group = group;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringField("name", group, true);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// TODO:(3) review but no danger if one could create a group
	// authManager.requiresPermission(getSession(), PermissionEnum.CREATE,
	// BaseControlEntity.getAdminEntity());
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	this.groupId = userDAO.createGroup(group);
	group.setId(groupId);

	getAuthorizationManager().addCreatorPermission(group, getSession());

	getApplog().info("Created Group " + group.getId() + ":" + group.getName());
    }

    public Long getGroupId() {
	return this.groupId;
    }
}
