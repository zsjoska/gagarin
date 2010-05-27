package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetControlEntityListForCategoryOP extends WebserviceOperation {

    private final String category;
    private RoleDAO roleDAO;
    private ControlEntityCategory categoryEnum;
    private List<WSControlEntity> controlEntities;

    public GetControlEntityListForCategoryOP(String sessionId, String category) {
	super(sessionId);
	this.category = category;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringValue(category, "category", 50);
	this.categoryEnum = ControlEntityCategory.valueOf(this.category);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	if (categoryEnum == ControlEntityCategory.ADMIN) {
	    this.controlEntities = new ArrayList<WSControlEntity>(1);
	    this.controlEntities.add(new WSControlEntity(BaseControlEntity.getAdminEntity()));
	    return;
	}

	List<ControlEntity> ceList = roleDAO.getControlEntityListForCategory(categoryEnum);
	this.controlEntities = WSConversionUtils.convertEntityList(ceList);
	getApplog().debug("Returning " + controlEntities.size() + " control entities");
    }

    public List<WSControlEntity> getControlEntities() {
	return this.controlEntities;
    }
}
