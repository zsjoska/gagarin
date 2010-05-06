package ro.gagarin.ws.userservice;

import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;

public class GetGroupUsersOP extends WebserviceOperation {

    public GetGroupUsersOP(String sessionId) {
	super(sessionId, GetGroupsOP.class);
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// TODO Auto-generated method stub

    }

    @Override
    public void execute() throws ExceptionBase {
	// TODO Auto-generated method stub

    }

    @Override
    public Statistic getStatistic() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	// TODO Auto-generated method stub

    }

}
