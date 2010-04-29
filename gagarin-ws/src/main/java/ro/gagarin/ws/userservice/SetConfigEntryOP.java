package ro.gagarin.ws.userservice;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.ConfigurationManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSConfig;

public class SetConfigEntryOP extends WebserviceOperation {

    private static final Statistic STAT_SET_CONFIG_ENTRY = new Statistic("ws.userserservice.setConfigEntry");
    private final WSConfig wsConfig;

    public SetConfigEntryOP(String sessionId, WSConfig wsConfig) {
	super(sessionId, SetConfigEntryOP.class);
	this.wsConfig = wsConfig;
    }

    @Override
    public void execute() throws ExceptionBase {
	AuthorizationManager authManager = FACTORY.getAuthorizationManager(getSession());

	authManager.requiresPermission(getSession(), PermissionEnum.ADMIN_OPERATION);

	ConfigurationManager cfgMgr = FACTORY.getConfigurationManager();
	cfgMgr.setConfigValue(getSession(), wsConfig);

    }

    @Override
    public Statistic getStatistic() {
	return STAT_SET_CONFIG_ENTRY;
    }

}
