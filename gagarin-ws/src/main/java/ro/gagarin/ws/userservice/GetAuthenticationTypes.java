package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthenticationManager;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.ws.executor.WebserviceOperation;

public class GetAuthenticationTypes extends WebserviceOperation {

    private AuthenticationManager authManager;
    private List<String> authenticatorNames;

    public GetAuthenticationTypes(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no param to check
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// I don't see reason to restrict
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthenticationManager();
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	authenticatorNames = authManager.getAuthenticatorNames();
    }

    public List<String> getAuthenticationTypes() {
	return this.authenticatorNames;
    }
}
