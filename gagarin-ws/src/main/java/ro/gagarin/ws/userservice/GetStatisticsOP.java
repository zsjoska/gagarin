package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.Statistic;
import ro.gagarin.utils.StatisticsContainer;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSStatistic;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetStatisticsOP extends WebserviceOperation {
    private final String filter;
    private List<WSStatistic> statisticsList;

    private AuthorizationManager authManager;

    public GetStatisticsOP(String sessionId, String filter) {
	super(sessionId);
	this.filter = filter;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
    }

    @Override
    public void execute() throws ExceptionBase {

	authManager.requiresPermission(getSession(), BaseControlEntity.getAdminEntity(), PermissionEnum.AUDIT);

	List<Statistic> statistics = StatisticsContainer.exportStatistics(filter);

	this.statisticsList = WSConversionUtils.convertToWSStatisticList(statistics);
    }

    public List<WSStatistic> getStatisticList() {
	return this.statisticsList;
    }

    @Override
    public String toString() {
	return "GetStatisticsOP [filter=" + filter + "]";
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// TODO:(2) add custom check for filter
    }

}
