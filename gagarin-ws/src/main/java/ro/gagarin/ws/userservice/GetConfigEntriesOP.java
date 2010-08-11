package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.config.ConfigEntry;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSConfig;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetConfigEntriesOP extends WebserviceOperation {

    private List<WSConfig> configList;
    private ConfigurationManager cfgMgr;

    public GetConfigEntriesOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, CommonControlEntities.ADMIN_CE, PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	cfgMgr = FACTORY.getConfigurationManager();
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	List<ConfigEntry> configValues = cfgMgr.getConfigValues();
	List<WSConfig> wsConfigList = WSConversionUtils.toWSConfigList(configValues);
	this.configList = wsConfigList;
	getApplog().debug("Returning " + configList.size() + " configs");
    }

    public List<WSConfig> getConfigEntries() {
	return configList;
    }
}
