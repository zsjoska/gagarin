package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSOwner;
import ro.gagarin.ws.util.WSUtil;

public class GetOwnersOP extends WebserviceOperation {

    private UserDAO userDAO;
    private ArrayList<WSOwner> owners;

    public GetOwnersOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = session.getManagerFactory().getDAOManager().getUserDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	this.owners = WSUtil.getOwnersList(userDAO);
	getApplog().debug("Returning " + owners.size() + " owners");
    }

    public List<WSOwner> getOwners() {
	return owners;
    }
}
