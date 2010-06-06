package ro.gagarin.model

import _root_.ro.gagarin.wsclient.WSClient
import _root_.net.liftweb.http.S._


object webServiceUtils {
  
  // TODO: move this URL to config file
  
  val wsclient = WSClient.getWSClient("http://localhost:8080/gagarin-ws/ws/")
  
  def authWSService = wsclient.getAuthService()
  def adminWSService = wsclient.getAdminService()
  
      def handleException(e : WSException_Exception) = {
	    val wsException = e.getFaultInfo()
	    error(wsException.getMessage +"(" + wsException.getDetail + ")");
	    if( wsException.getErrorCode() == ErrorCodes.SESSION_NOT_FOUND ){
	      wsSession.set(null)
	      redirectTo("/login")
	    } else if(wsException.getErrorCode() == ErrorCodes.LOGIN_REQUIRED){
	      wsSession.set(null)
	      redirectTo("/")
	    } else {
	      redirectTo("/")
	    }
    }  
}

