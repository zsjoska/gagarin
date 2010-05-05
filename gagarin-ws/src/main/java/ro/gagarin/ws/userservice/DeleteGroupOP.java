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

public class DeleteGroupOP extends WebserviceOperation {

    private static final Statistic STAT = Statistic.getByName("ws.userserservice.deleteGroup");

    private final WSGroup group;

    private AuthorizationManager authManager;

    private UserDAO userManager;

    public DeleteGroupOP(String sessionId, WSGroup group) {
	super(sessionId, DeleteGroupOP.class);
	this.group = group;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// TODO: implement combined groupname & id verification
	FieldValidator.requireLongField("id", group);
    }

    @Override
    public void execute() throws ExceptionBase {
	// the session user must have LIST_GROUPS permission
	authManager.requiresPermission(getSession(), PermissionEnum.DELETE_GROUP);

	userManager.deleteGroup(this.group);
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
