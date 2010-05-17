package ro.gagarin.wsclient;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import ro.gagarin.Admin;
import ro.gagarin.AdminService;
import ro.gagarin.Authentication;
import ro.gagarin.AuthenticationService;

public class WSClient {

    private final String rootURL;
    private Authentication authentication;
    private Admin userService;

    private WSClient(String rootURL) throws MalformedURLException {
	new URL(rootURL);
	this.rootURL = rootURL;
    }

    public static WSClient getWSClient(String rootURL) throws MalformedURLException {
	return new WSClient(rootURL);
    }

    public synchronized Authentication getAuthentication() {
	if (this.authentication == null) {
	    AuthenticationService service;
	    try {
		service = new AuthenticationService(new URL(this.rootURL + "AuthService" + "?wsdl"), new QName(
			"http://ws.gagarin.ro/", "AuthenticationService"));
		this.authentication = service.getAuthenticationPort();
	    } catch (MalformedURLException e) {
		e.printStackTrace();
	    }
	}
	return this.authentication;
    }

    public synchronized Admin getUserService() {
	if (this.userService == null) {
	    AdminService service;
	    try {
		service = new AdminService(new URL(this.rootURL + "UserService" + "?wsdl"), new QName(
			"http://ws.gagarin.ro/", "UserServiceService"));
		this.userService = service.getAdminPort();
	    } catch (MalformedURLException e) {
		e.printStackTrace();
	    }
	}
	return this.userService;
    }
}
