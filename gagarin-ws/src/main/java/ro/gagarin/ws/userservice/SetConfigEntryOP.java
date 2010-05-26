package ro.gagarin.ws.userservice;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.ConfigurationManager;
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
    protected void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	authManager.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.ADMIN);

	ConfigurationManager cfgMgr = FACTORY.getConfigurationManager();
	cfgMgr.setConfigValue(getSession(), wsConfig);
	getApplog().info("Config update:" + wsConfig);

    }

    @Override
    public String toString() {
	return "SetConfigEntryOP [wsConfig=" + wsConfig + "]";
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringField("configName", wsConfig, true);
	FieldValidator.requireStringField("configValue", wsConfig, true);
    }
}
