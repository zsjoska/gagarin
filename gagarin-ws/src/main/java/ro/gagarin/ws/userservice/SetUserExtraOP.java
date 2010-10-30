package ro.gagarin.ws.userservice;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.UserExtraDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSPropertySet;

public class SetUserExtraOP extends WebserviceOperation {

    private final WSPropertySet userExtra;
    private UserExtraDAO userExtraDAO;

    public SetUserExtraOP(String sessionId, WSPropertySet userExtra) {
	super(sessionId);
	this.userExtra = userExtra;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireIdField(userExtra);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// the current user has the right to view it's own properties
	if (session.getUser().getId() != this.userExtra.getId()) {
	    authMgr.requiresPermission(session, CommonControlEntities.USER_CE, PermissionEnum.UPDATE);
	}
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userExtraDAO = session.getManagerFactory().getDAOManager().getUserExtraDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	userExtraDAO.updateRecord(this.userExtra);
    }

}
