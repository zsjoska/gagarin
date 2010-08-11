package ro.gagarin.ws.userservice;

import ro.gagarin.CommonControlEntities;
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
    private ConfigurationManager cfgMgr;

    public SetConfigEntryOP(String sessionId, WSConfig wsConfig) {
	super(sessionId);
	this.wsConfig = wsConfig;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringField("configName", wsConfig, true);
	FieldValidator.requireStringField("configValue", wsConfig, true);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, CommonControlEntities.ADMIN_CE, PermissionEnum.ADMIN);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	cfgMgr = FACTORY.getConfigurationManager();
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	cfgMgr.setConfigValue(getSession(), wsConfig);
	getApplog().info("Config update:" + wsConfig);

    }

    @Override
    public String toString() {
	return "SetConfigEntryOP [wsConfig=" + wsConfig + "]";
    }
}
