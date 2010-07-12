package ro.gagarin.ws.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.ws.objects.WSOwner;

public class WSUtil {

    public static ArrayList<WSOwner> getOwnersList(UserDAO userDAO) throws OperationException {
	ArrayList<WSOwner> owners = new ArrayList<WSOwner>();
	List<Group> groups = userDAO.getGroups();
	List<User> users = userDAO.getAllUsers();
	for (Group group : groups) {
	    owners.add(new WSOwner(group));
	}
	for (User user : users) {
	    owners.add(new WSOwner(user));
	}
	return owners;
    }

    public static Map<Long, WSOwner> getOwnerMap(UserDAO userDAO) throws OperationException {
	ArrayList<WSOwner> ownerList = getOwnersList(userDAO);
	Map<Long, WSOwner> ownerMap = new HashMap<Long, WSOwner>();
	for (WSOwner owner : ownerList) {
	    ownerMap.put(owner.getId(), owner);
	}
	return ownerMap;
    }

    public static Set<PermissionEnum> createAllPermissionSet() {
	HashSet<PermissionEnum> perms = new HashSet<PermissionEnum>();
	for (PermissionEnum perm : PermissionEnum.values()) {
	    perms.add(perm);
	}
	return perms;
    }
}
