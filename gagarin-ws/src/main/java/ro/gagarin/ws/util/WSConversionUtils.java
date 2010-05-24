package ro.gagarin.ws.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.gagarin.ControlEntity;
import ro.gagarin.config.ConfigEntry;
import ro.gagarin.log.LogEntry;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.objects.WSConfig;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.objects.WSStatistic;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;

public class WSConversionUtils {

    public static List<WSUserPermission> convertToWSPermissionList(Collection<UserPermission> permissions) {
	ArrayList<WSUserPermission> list = new ArrayList<WSUserPermission>();
	for (UserPermission userPermission : permissions) {
	    list.add(new WSUserPermission(userPermission));
	}
	return list;
    }

    public static List<WSUser> convertToWSUserList(Collection<User> users) {
	ArrayList<WSUser> list = new ArrayList<WSUser>();
	for (User user : users) {
	    list.add(new WSUser(user));
	}
	return list;
    }

    public static List<WSConfig> toWSConfigList(Collection<ConfigEntry> configValues) {
	ArrayList<WSConfig> list = new ArrayList<WSConfig>();
	for (ConfigEntry configEntry : configValues) {
	    list.add(new WSConfig(configEntry));
	}
	return list;
    }

    public static List<WSLogEntry> toWSLogList(Collection<LogEntry> logValues) {
	ArrayList<WSLogEntry> list = new ArrayList<WSLogEntry>();
	for (LogEntry logEntry : logValues) {
	    list.add(new WSLogEntry(logEntry));
	}
	return list;
    }

    public static Set<WSUserPermission> convertToWSPermissionSet(Collection<UserPermission> perm) {
	HashSet<WSUserPermission> set = new HashSet<WSUserPermission>();
	for (UserPermission userPermission : perm) {
	    set.add(new WSUserPermission(userPermission));
	}
	return set;
    }

    public static List<WSExportedSession> convertToSessionList(List<Session> sessions) {
	ArrayList<WSExportedSession> list = new ArrayList<WSExportedSession>();
	for (Session logEntry : sessions) {
	    list.add(new WSExportedSession(logEntry));
	}
	return list;
    }

    public static List<WSStatistic> convertToWSStatisticList(List<Statistic> statistics) {
	ArrayList<WSStatistic> list = new ArrayList<WSStatistic>();
	for (Statistic stat : statistics) {
	    list.add(new WSStatistic(stat));
	}
	return list;
    }

    public static List<WSGroup> convertToGroupList(List<Group> groups) {
	ArrayList<WSGroup> list = new ArrayList<WSGroup>();
	for (Group group : groups) {
	    list.add(new WSGroup(group));
	}
	return list;
    }

    public static HashMap<WSControlEntity, Set<PermissionEnum>> convertEffectivePermissions(
	    Map<ControlEntity, Set<PermissionEnum>> effectivePermissions) {
	// TODO Auto-generated method stub
	return null;
    }

}
