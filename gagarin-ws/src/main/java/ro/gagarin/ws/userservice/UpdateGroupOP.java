package ro.gagarin.ws.userservice;

import java.security.acl.Group;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;

public class UpdateGroupOP extends WebserviceOperation {

    private static final Statistic STAT = Statistic.getByName("ws.userserservice.deleteGroup");

    private final WSGroup group;

    private AuthorizationManager authManager;

    private UserDAO userManager;

    public UpdateGroupOP(String sessionId, WSGroup group) {
	super(sessionId, UpdateGroupOP.class);
	this.group = group;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// id is required for identification
	FieldValidator.requireLongField("id", group);

	if (group.getName() == null && group.getDescription() == null) {
	    throw new FieldRequiredException("name or description", Group.class);
	}

	if (group.getName() != null) {
	    FieldValidator.requireStringField("name", group, true);
	}
	if (group.getDescription() != null) {
	    FieldValidator.requireStringField("description", group, false);
	}
    }

    @Override
    public void execute() throws ExceptionBase {
	authManager.requiresPermission(getSession(), PermissionEnum.UPDATE_GROUP);
	userManager.updateGroup(this.group);
    }

    @Override
    public Statistic getStatistic() {
	return STAT;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
	userManager = FACTORY.getDAOManager().getUserDAO(getSession());
    }

}
