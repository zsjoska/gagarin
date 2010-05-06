package ro.gagarin.ws.userservice;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;

public class CreateGroupOP extends WebserviceOperation {

    private static final Statistic STAT_CREATE_GROUP = Statistic.getByName("ws.userserservice.createGroup");

    private AuthorizationManager authManager;
    private UserDAO userManager;

    private final WSGroup group;

    private Long groupId = null;

    public CreateGroupOP(String sessionId, WSGroup group) {
	super(sessionId, CreateGroupOP.class);
	this.group = group;
    }

    @Override
    public void execute() throws ExceptionBase {
	// the session user must have CREATE_GROUP permission
	authManager.requiresPermission(getSession(), PermissionEnum.CREATE_GROUP);

	this.groupId = userManager.createGroup(group);
	getApplog().info("Created User " + group.getId() + ":" + group.getName());
    }

    @Override
    public Statistic getStatistic() {
	return STAT_CREATE_GROUP;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
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