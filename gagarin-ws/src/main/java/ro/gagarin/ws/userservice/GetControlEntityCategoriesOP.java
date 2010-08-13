package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;

public class GetControlEntityCategoriesOP extends WebserviceOperation {

    private List<ControlEntityCategory> controlEntities;

    public GetControlEntityCategoriesOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, CommonControlEntities.PERMISSION_CE, PermissionEnum.LIST);
	// no permission requirement
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	this.controlEntities = new ArrayList<ControlEntityCategory>();
	for (ControlEntityCategory ceCat : ControlEntityCategory.values()) {
	    controlEntities.add(ceCat);
	}
	getApplog().debug("Returning " + controlEntities.size() + " control entities");
    }

    public List<ControlEntityCategory> getControlEntities() {
	return this.controlEntities;
    }
}
