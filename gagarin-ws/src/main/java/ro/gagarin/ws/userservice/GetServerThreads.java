package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.ScheduleManager;
import ro.gagarin.scheduler.SchedulerThread;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSThread;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetServerThreads extends WebserviceOperation {

    private ScheduleManager scheduleManager;
    private List<WSThread> threads;

    public GetServerThreads(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, CommonControlEntities.ADMIN_CE, PermissionEnum.LIST);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	List<SchedulerThread> exportThreads = scheduleManager.exportThreads();
	this.threads = WSConversionUtils.convertToWSThreads(exportThreads);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	scheduleManager = FACTORY.getScheduleManager();
    }

    public List<WSThread> getServerThreads() {
	return this.threads;
    }

}
