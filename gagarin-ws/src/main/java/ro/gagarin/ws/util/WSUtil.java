package ro.gagarin.ws.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.ws.objects.WSPerson;

public class WSUtil {

    public static ArrayList<WSPerson> getPersonList(UserDAO userDAO) throws OperationException {
	ArrayList<WSPerson> persons = new ArrayList<WSPerson>();
	List<Group> groups = userDAO.getGroups();
	List<User> users = userDAO.getAllUsers();
	for (Group group : groups) {
	    persons.add(new WSPerson(group));
	}
	for (User user : users) {
	    persons.add(new WSPerson(user));
	}
	return persons;
    }

    public static Map<Long, WSPerson> getPersonMap(UserDAO userDAO) throws OperationException {
	ArrayList<WSPerson> personList = getPersonList(userDAO);
	Map<Long, WSPerson> personMap = new HashMap<Long, WSPerson>();
	for (WSPerson person : personList) {
	    personMap.put(person.getId(), person);
	}
	return personMap;
    }
}
