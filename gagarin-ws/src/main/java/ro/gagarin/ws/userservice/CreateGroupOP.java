package ro.gagarin.ws.userservice;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;

public class CreateGroupOP extends WebserviceOperation {

    private AuthorizationManager authManager;
    private UserDAO userManager;

    private final WSGroup group;

    private Long groupId = null;

    public CreateGroupOP(String sessionId, WSGroup group) {
	super(sessionId);
	this.group = group;
    }

    @Override
    public void execute() throws ExceptionBase {
	// TODO: review but no danger if one could create a group
	// // the session user must have CREATE_GROUP permission
	// authManager.requiresPermission(getSession(), PermissionEnum.CREATE,
	// BaseControlEntity.getAdminEntity());

	this.groupId = userManager.createGroup(group);
	getApplog().info("Created User " + group.getId() + ":" + group.getName());
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	userManager = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    public Long getGroupId() {
	return this.groupId;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringField("name", group, true);
    }

}
