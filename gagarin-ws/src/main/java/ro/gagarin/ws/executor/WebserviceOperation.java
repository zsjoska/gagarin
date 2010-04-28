package ro.gagarin.ws.executor;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;
import ro.gagarin.utils.Statistic;

public abstract class WebserviceOperation {

    protected static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private final String strSession;

    private Session session = null;

    private AppLog applog;

    private final Class<?> opClass;

    public WebserviceOperation(String session, Class<?> opClass) {
	this.strSession = session;
	this.opClass = opClass;
    }

    public abstract void execute() throws ExceptionBase;

    public abstract Statistic getStatistic();

    public Session getSession() {
	return session;
    }

    public AppLog getApplog() {
	return applog;
    }

    public void prepareSession() throws SessionNotFoundException {
	SessionManager sessionManager = FACTORY.getSessionManager();
	this.session = sessionManager.acquireSession(this.strSession);
	this.applog = session.getManagerFactory().getLogManager(session, opClass);
    }

    public void prepare() {
    }

    public void finish() {
    }

    public void releaseSession() {
	FACTORY.releaseSession(this.session);
    }
}
