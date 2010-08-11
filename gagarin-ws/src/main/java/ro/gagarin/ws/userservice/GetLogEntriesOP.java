package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.log.AppLog;
import ro.gagarin.log.LogEntry;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.Admin;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetLogEntriesOP extends WebserviceOperation {

    private final String username;
    private List<WSLogEntry> logList;

    private AppLog logMgr;

    public GetLogEntriesOP(String sessionId, String user) {
	super(sessionId);
	this.username = user;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// if the username is null, all logs are retrieved
	if (username != null) {
	    FieldValidator.requireStringValue(username, "username", 50);
	}
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(getSession(), CommonControlEntities.ADMIN_CE, PermissionEnum.AUDIT);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	logMgr = FACTORY.getLogManager().getLoggingSession(getSession(), Admin.class);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	List<LogEntry> logValues = logMgr.getLogEntries(username);
	List<WSLogEntry> wsConfigList = WSConversionUtils.toWSLogList(logValues);
	this.logList = wsConfigList;
	getApplog().debug("Returning " + logList.size() + " log entries");
    }

    public List<WSLogEntry> getLogEntries() {
	return logList;
    }

    @Override
    public String toString() {
	return "GetLogEntriesOP [user=" + username + "]";
    }
}
