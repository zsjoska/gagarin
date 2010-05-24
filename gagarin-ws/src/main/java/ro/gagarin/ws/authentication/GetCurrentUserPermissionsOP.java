package ro.gagarin.ws.authentication;

import java.util.HashMap;
import java.util.Set;

import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetCurrentUserPermissionsOP extends WebserviceOperation {

    private HashMap<WSControlEntity, Set<PermissionEnum>> permissions;

    public GetCurrentUserPermissionsOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
    }

    @Override
    public void execute() throws ExceptionBase {
	this.permissions = WSConversionUtils.convertEffectivePermissions(getSession().getEffectivePermissions());
    }

    public HashMap<WSControlEntity, Set<PermissionEnum>> getCurrentUserPermissions() {
	return this.permissions;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }

}
