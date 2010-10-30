package ro.gagarin.ws.userservice;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.UserExtraDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserExtraRecord;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSPropertySet;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetUserExtraOP extends WebserviceOperation {

    private final WSUser user;
    private UserExtraDAO userExtraDAO;
    private WSPropertySet propertySet;

    public GetUserExtraOP(String sessionId, WSUser user) {
	super(sessionId);
	this.user = user;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	if (this.user == null) {
	    throw new FieldRequiredException("user", User.class);
	}
	FieldValidator.requireLongField("id", this.user);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// the current user has the right to view it's own properties
	if (session.getUser().getId() != this.user.getId()) {
	    authMgr.requiresPermission(session, CommonControlEntities.USER_CE, PermissionEnum.VIEW);
	}
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userExtraDAO = session.getManagerFactory().getDAOManager().getUserExtraDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	UserExtraRecord record = userExtraDAO.getRecord(this.user.getId());
	propertySet = WSConversionUtils.convertToWSPropertySet(record);
    }

    public WSPropertySet getUserExtra() {
	return this.propertySet;
    }

}
