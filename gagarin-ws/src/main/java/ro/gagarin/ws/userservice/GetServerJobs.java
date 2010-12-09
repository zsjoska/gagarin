package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.ScheduleManager;
import ro.gagarin.scheduler.JobController;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSJob;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetServerJobs extends WebserviceOperation {

    private ScheduleManager scheduleManager;
    private List<WSJob> jobs;

    public GetServerJobs(String sessionId) {
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
	List<JobController> exportJobs = scheduleManager.exportJobs();
	this.jobs = WSConversionUtils.convertToWSJobs(exportJobs);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	scheduleManager = FACTORY.getScheduleManager();
    }

    public List<WSJob> getServerJobs() {
	return this.jobs;
    }

}
