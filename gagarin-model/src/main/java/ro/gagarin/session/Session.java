package ro.gagarin.session;

import java.util.HashMap;

import ro.gagarin.BaseDAO;
import ro.gagarin.ManagerFactory;
import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.User;

public class Session extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3676125918206947624L;
	private long sessionTimeout = 0;
	private long expires;
	private String language;
	private String reason;
	private User user;
	private BaseDAO manager;
	private String sessionString;
	private boolean busy;

	HashMap<String, Object> properties = new HashMap<String, Object>();
	private ManagerFactory managerFactory;
	private Throwable t;

	public Session() {
		// TODO: make the reason mandatory
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

	public BaseDAO getManager() {
		return this.manager;
	}

	public void setManager(BaseDAO manager) {
		this.manager = manager;
	}

	@Override
	public String toString() {
		if (this.user == null) {
			return "<unbound>:" + getId();
		}
		return this.getUser().getUsername() + ":" + this.getId();
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
}
