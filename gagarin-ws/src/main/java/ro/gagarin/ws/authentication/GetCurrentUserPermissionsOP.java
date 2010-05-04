package ro.gagarin.ws.authentication;

import java.util.Set;

import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetCurrentUserPermissionsOP extends WebserviceOperation {

    private static final Statistic STAT_CURRENT_USER_PERMISSION = Statistic
	    .getByName("ws.auth.getCurrentUserPermissions");
    private Set<WSUserPermission> currentUserPermissions;
    private RoleDAO roleDAO;

    public GetCurrentUserPermissionsOP(String sessionId) {
	super(sessionId, GetCurrentUserPermissionsOP.class);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());

    }

    @Override
    public void execute() throws ExceptionBase {

	Set<UserPermission> perm = roleDAO.getRolePermissions(getSession().getUser().getRole());
	this.currentUserPermissions = WSConversionUtils.convertToWSPermissionSet(perm);

    }

    @Override
    public Statistic getStatistic() {
	return STAT_CURRENT_USER_PERMISSION;
    }

    public Set<WSUserPermission> getCurrentUserPermissions() {
	return currentUserPermissions;
    }

}
