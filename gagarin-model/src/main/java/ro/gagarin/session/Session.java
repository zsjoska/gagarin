package ro.gagarin.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ro.gagarin.BaseEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.dao.BaseDAO;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserExtraRecord;

public class Session extends BaseEntity {

    private long sessionTimeout = 0;
    private long expires;
    private String language;
    private String reason;
    private User user;
    private BaseDAO dao;
    private String sessionString;
    private boolean busy;

    private boolean adminSession = false;

    HashMap<String, Object> properties = new HashMap<String, Object>();
    private ManagerFactory managerFactory;
    private Throwable t;
    private Map<ControlEntity, Set<PermissionEnum>> effectivePermissions;

    // TODO:(5) add as cached value
    private UserExtraRecord userExtra;

    public Session() {
	// TODO:(3) make the reason mandatory
    }

    public Session(Session clone) {
	this.sessionTimeout = clone.getSessionTimeout();
	this.expires = clone.getExpires();
	this.language = clone.getLanguage();
	this.reason = clone.getReason();
	this.user = clone.getUser();
	this.sessionString = clone.getSessionString();
	this.busy = clone.isBusy();
	this.adminSession = clone.isAdminSession();
	this.properties = (HashMap<String, Object>) clone.properties.clone();
	this.managerFactory = clone.getManagerFactory();
	this.t = clone.t; // will be set when marking busy
	this.effectivePermissions = clone.getEffectivePermissions();
    }

    public Session(ManagerFactory managerFactory) {
	this.managerFactory = managerFactory;
    }

    public void setLanguage(String language) {
	this.language = language;
    }

    public String getLanguage() {
	return language;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public String getReason() {
	return reason;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public User getUser() {
	return user;
    }

    public void setExpires(long expires) {
	this.expires = expires;
    }

    public long getExpires() {
	return expires;
    }

    public boolean isExpired() {
	return getExpires() < System.currentTimeMillis();
    }

    public void setSessiontimeout(long sessiontimeout) {
	this.sessionTimeout = sessiontimeout;
    }

    public long getSessionTimeout() {
	return sessionTimeout;
    }

    public BaseDAO getDAO() {
	return this.dao;
    }

    public void setManager(BaseDAO dao) {
	this.dao = dao;
    }

    @Override
    public String toString() {
	if (this.user == null) {
	    return this.reason + ":" + getSessionString();
	}
	return this.getUser().getUsername() + "@" + this.reason + ":" + this.getSessionString();
    }

    public void setSessionString(String id) {
	this.sessionString = id;
    }

    public String getSessionString() {
	return sessionString;
    }

    public void setProperty(Class<?> owner, Object object) {
	this.properties.put(owner.getName(), object);
    }

    public Object getProperty(Class<?> owner) {
	return this.properties.get(owner.getName());
    }

    public void setBusy(boolean busy, Throwable t) {
	this.busy = busy;
	this.t = t;
    }

    public boolean isBusy() {
	return busy;
    }

    public void setManagerFactory(ManagerFactory factory) {
	this.managerFactory = factory;
    }

    public ManagerFactory getManagerFactory() {
	return this.managerFactory;
    }

    public Throwable getCreationStacktrace() {

	return this.t;
    }

    public void assignUser(User user, Map<ControlEntity, Set<PermissionEnum>> permMap, UserExtraRecord record) {
	this.userExtra = record;
	this.effectivePermissions = permMap;
	setUser(user);
    }

    public Map<ControlEntity, Set<PermissionEnum>> getEffectivePermissions() {
	return this.effectivePermissions;
    }

    public void setAdminSession(boolean adminSession) {
	this.adminSession = adminSession;
    }

    public boolean isAdminSession() {
	return adminSession;
    }

    public UserExtraRecord getUserExtra() {
	if (this.userExtra != null) {
	    return userExtra;
	}
	return null;
    }
}
