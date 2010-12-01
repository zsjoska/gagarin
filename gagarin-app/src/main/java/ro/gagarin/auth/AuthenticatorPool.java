package ro.gagarin.auth;

import java.util.Collection;
import java.util.HashMap;

public class AuthenticatorPool {

    private static final HashMap<String, Authenticator> AUTHENTICATORS = new HashMap<String, Authenticator>();

    private static Authenticator defAuthenticator = null;

    public static void addAuthenticator(String name, Authenticator authenticator) {
	AUTHENTICATORS.put(name, authenticator);
	if (defAuthenticator == null) {
	    defAuthenticator = authenticator;
	}
    }

    public static Authenticator getAuthenticatorForName(String name) {
	return AUTHENTICATORS.get(name);
    }

    public static Authenticator getDefaultAuthenticator() {
	return defAuthenticator;
    }

    public static Collection<Authenticator> getAuthenticators() {
	return AUTHENTICATORS.values();
    }
}
