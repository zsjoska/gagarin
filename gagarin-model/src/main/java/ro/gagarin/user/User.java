package ro.gagarin.user;

public interface User {

	public abstract String getUsername();

	public abstract String getPassword();

	public abstract String getName();

	public abstract Long getId();

	public abstract UserRole getRole();

}