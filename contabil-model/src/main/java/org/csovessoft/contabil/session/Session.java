package org.csovessoft.contabil.session;

import org.csovessoft.contabil.user.BaseEntity;
import org.csovessoft.contabil.user.User;

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

	public Session() {
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

}
