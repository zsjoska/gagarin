package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.ControlEntityCategory;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.ws.executor.WebserviceOperation;

public class GetControlEntityCategoriesOP extends WebserviceOperation {

    private List<ControlEntityCategory> controlEntities;

    public GetControlEntityCategoriesOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
    }

    @Override
    public void execute() throws ExceptionBase {
	this.controlEntities = new ArrayList<ControlEntityCategory>();
	for (ControlEntityCategory ceCat : ControlEntityCategory.values()) {
	    controlEntities.add(ceCat);
	}

    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
    }

    public List<ControlEntityCategory> getControlEntities() {
	return this.controlEntities;
    }

}
