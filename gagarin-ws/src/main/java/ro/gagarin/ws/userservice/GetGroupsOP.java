package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetGroupsOP extends WebserviceOperation {
    private static final Statistic STAT = Statistic.getByName("ws.userserservice.getGroups");

    private AuthorizationManager authManager;
    private UserDAO userManager;
    private List<WSGroup> groups;

    public GetGroupsOP(String sessionId) {
	super(sessionId, GetGroupsOP.class);
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input to check
    }

    @Override
    public void execute() throws ExceptionBase {
	// the session user must have LIST_GROUPS permission
	authManager.requiresPermission(getSession(), PermissionEnum.LIST_GROUPS);

	groups = WSConversionUtils.convertToGroupList(userManager.getGroups());
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

    public List<WSGroup> getGroups() {
	return this.groups;
    }

}
