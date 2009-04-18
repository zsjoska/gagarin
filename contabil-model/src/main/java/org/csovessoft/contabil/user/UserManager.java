package org.csovessoft.contabil.user;

import org.csovessoft.contabil.results.MLoginResult;


public interface UserManager {

	MLoginResult login(String username, String password);

}
