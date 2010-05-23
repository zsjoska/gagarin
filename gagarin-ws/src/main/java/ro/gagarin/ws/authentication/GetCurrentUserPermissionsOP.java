package ro.gagarin.ws.authentication;

import java.util.Set;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;

// TODO: completly rewrite and refactor: the effective permissions should be returned here
public class GetCurrentUserPermissionsOP extends WebserviceOperation {

    private Set<WSUserPermission> currentUserPermissions;
    private RoleDAO roleDAO;

    public GetCurrentUserPermissionsOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());

    }

    @Override
    public void execute() throws ExceptionBase {

    }

    public Set<WSUserPermission> getCurrentUserPermissions() {
	return currentUserPermissions;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }

}
