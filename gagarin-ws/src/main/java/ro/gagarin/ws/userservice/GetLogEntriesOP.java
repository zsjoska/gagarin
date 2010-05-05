package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.log.AppLog;
import ro.gagarin.log.LogEntry;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.UserService;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetLogEntriesOP extends WebserviceOperation {

    private static final Statistic STAT_GET_LOG_ENTRIES = Statistic.getByName("ws.userserservice.getLogEntries");

    private final String username;
    private List<WSLogEntry> configList;

    private AuthorizationManager authManager;

    private AppLog logMgr;

    public GetLogEntriesOP(String sessionId, String user) {
	super(sessionId, GetLogEntriesOP.class);
	this.username = user;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
	logMgr = FACTORY.getLogManager(getSession(), UserService.class);

    }

    @Override
    public void execute() throws ExceptionBase {

	authManager.requiresPermission(getSession(), PermissionEnum.ADMIN_OPERATION);

	List<LogEntry> logValues = logMgr.getLogEntries(username);
	List<WSLogEntry> wsConfigList = WSConversionUtils.toWSLogList(logValues);
	this.configList = wsConfigList;
    }

    @Override
    public Statistic getStatistic() {
	return STAT_GET_LOG_ENTRIES;
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
	// TODO: implement custom verification for username
    }
}
