package ro.gagarin.auth;

public abstract class BaseAuthenticator implements Authenticator {

    private final String name;

    public BaseAuthenticator(String name) {
	this.name = name;
	AuthenticatorPool.addAuthenticator(name, this);
    }

    @Override
    public String getName() {
	return name;
    }
}
