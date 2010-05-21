package ro.gagarin.ws.userservice;

import java.util.List;

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
    private AuthorizationManager authManager;
    private ConfigurationManager cfgMgr;

    public GetConfigEntriesOP(String sessionId) {
	super(sessionId);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	cfgMgr = FACTORY.getConfigurationManager();
    }

    @Override
    public void execute() throws ExceptionBase {

	authManager.requiresPermission(getSession(), PermissionEnum.ADMIN_OPERATION);

	List<ConfigEntry> configValues = cfgMgr.getConfigValues();
	List<WSConfig> wsConfigList = WSConversionUtils.toWSConfigList(configValues);
	this.configList = wsConfigList;
    }

    public List<WSConfig> getConfigEntries() {
	return configList;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	// no input
    }
}
