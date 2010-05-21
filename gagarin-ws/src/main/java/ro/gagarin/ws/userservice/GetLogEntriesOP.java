package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.BaseControlEntity;
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
    private List<WSLogEntry> configList;

    private AuthorizationManager authManager;

    private AppLog logMgr;

    public GetLogEntriesOP(String sessionId, String user) {
	super(sessionId);
	this.username = user;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	logMgr = FACTORY.getLogManager().getLoggingSession(getSession(), Admin.class);

    }

    @Override
    public void execute() throws ExceptionBase {

	authManager.requiresPermission(getSession(), PermissionEnum.AUDIT, BaseControlEntity.getAdminEntity());

	List<LogEntry> logValues = logMgr.getLogEntries(username);
	List<WSLogEntry> wsConfigList = WSConversionUtils.toWSLogList(logValues);
	this.configList = wsConfigList;
    }

    public List<WSLogEntry> getLogEntries() {
	return configList;
    }

    @Override
    public String toString() {
	return "GetLogEntriesOP [user=" + username + "]";
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// if the username is null, all logs are retrieved
	if (username != null) {
	    FieldValidator.checkStringValue(username, "username", 50);
	}
    }
}
