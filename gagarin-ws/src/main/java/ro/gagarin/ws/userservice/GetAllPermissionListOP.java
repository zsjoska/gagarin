package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetAllPermissionListOP extends WebserviceOperation {

    private List<WSUserPermission> permissionlist;
    private RoleDAO roleDAO;

    public GetAllPermissionListOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = FACTORY.getDAOManager().getRoleDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	List<UserPermission> allPermissions = roleDAO.getAllPermissions();
	this.permissionlist = WSConversionUtils.convertToWSPermissionList(allPermissions);
    }

    public List<WSUserPermission> getPermissionList() {
	return this.permissionlist;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input
    }
}
