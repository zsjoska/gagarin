package org.csovessoft.contabil.session;

import org.csovessoft.contabil.user.Entity;
import org.csovessoft.contabil.user.User;

public class Session extends Entity {
	private String language;
	private String reason;
	private User user;

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

}
