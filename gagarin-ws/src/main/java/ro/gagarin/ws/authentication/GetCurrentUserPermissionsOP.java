package ro.gagarin.ws.authentication;

import java.util.List;

import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSEffectivePermission;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetCurrentUserPermissionsOP extends WebserviceOperation {

    private List<WSEffectivePermission> permissions;

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

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }

    public List<WSEffectivePermission> getCurrentUserPermissions() {
	return this.permissions;
    }

}
