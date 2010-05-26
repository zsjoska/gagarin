package ro.gagarin.ws.userservice;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSPerson;

public class GetPersonsOP extends WebserviceOperation {

    private UserDAO userDAO;
    private ArrayList<WSPerson> persons;

    public GetPersonsOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
    }

    @Override
    public void execute() throws ExceptionBase {
	List<Group> groups = userDAO.getGroups();
	List<User> users = userDAO.getAllUsers();
	persons = new ArrayList<WSPerson>();
	for (Group group : groups) {
	    persons.add(new WSPerson(group));
	}
	for (User user : users) {
	    persons.add(new WSPerson(user));
	}
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	userDAO = session.getManagerFactory().getDAOManager().getUserDAO(session);
    }

    public List<WSPerson> getPersons() {
	return persons;
    }

}
