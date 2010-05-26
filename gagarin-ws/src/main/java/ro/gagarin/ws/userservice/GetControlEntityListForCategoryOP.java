package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
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
    public void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringValue(category, "category", 50);
	this.categoryEnum = ControlEntityCategory.valueOf(this.category);
    }

    @Override
    public void execute() throws ExceptionBase {
	if (categoryEnum == ControlEntityCategory.ADMIN) {
	    this.controlEntities = new ArrayList<WSControlEntity>(1);
	    this.controlEntities.add(new WSControlEntity(BaseControlEntity.getAdminEntity()));
	    return;
	}

	List<ControlEntity> ceList = roleDAO.getControlEntityListForCategory(categoryEnum);
	this.controlEntities = WSConversionUtils.convertEntityList(ceList);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
    }

    public List<WSControlEntity> getControlEntities() {
	return this.controlEntities;
    }

}
