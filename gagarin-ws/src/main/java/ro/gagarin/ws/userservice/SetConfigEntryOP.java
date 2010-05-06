package ro.gagarin.ws.userservice;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.ConfigurationManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSConfig;

public class SetConfigEntryOP extends WebserviceOperation {

    private final WSConfig wsConfig;
    private AuthorizationManager authManager;

    public SetConfigEntryOP(String sessionId, WSConfig wsConfig) {
	super(sessionId);
	this.wsConfig = wsConfig;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
    }

    @Override
    public void execute() throws ExceptionBase {

	authManager.requiresPermission(getSession(), PermissionEnum.ADMIN_OPERATION);

	ConfigurationManager cfgMgr = FACTORY.getConfigurationManager();
	cfgMgr.setConfigValue(getSession(), wsConfig);
	getApplog().info("Config update:" + wsConfig);

    }

    @Override
    public String toString() {
	return "SetConfigEntryOP [wsConfig=" + wsConfig + "]";
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringField("configName", wsConfig, true);
	FieldValidator.requireStringField("configValue", wsConfig, true);
    }
}
