package ro.gagarin.ws.objects;

import ro.gagarin.session.Session;

public class WSExportedSession {

    private String sessionid;
    private String username;
    private String reason;
    private long expires;

    public WSExportedSession() {
	// Constructor required for WS export
    }

    public WSExportedSession(Session session) {
	this.sessionid = session.getSessionString();
	if (session.getUser() != null) {
	    this.username = session.getUser().getUsername();
	} else {
	    this.username = "UNKNOWN";
	}
	this.reason = session.getReason();
	this.expires = session.getExpires();
    }

    public String getSessionid() {
	return sessionid;
    }

    public void setSessionid(String sessionid) {
	this.sessionid = sessionid;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public long getExpires() {
	return expires;
    }

    public void setExpires(long expires) {
	this.expires = expires;
    }

    @Override
    public String toString() {
	return "WSExportedSession [expires=" + expires + ", reason=" + reason + ", sessionid=" + sessionid
		+ ", username=" + username + "]";
    }
}
