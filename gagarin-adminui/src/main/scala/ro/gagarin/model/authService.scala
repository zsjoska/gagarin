
package ro.gagarin.model

import scala.collection.JavaConversions._
import net.liftweb.http.S._
import ro.gagarin.wsclient.WSClient
import ro.gagarin.model.webServiceUtils._
import ro.gagarin._

object authService {
  
  def logout = {
	  try{
		authWSService.logout(wsSession.session)
		wsSession.set(null)
	  } catch {
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

    def createSession(lang: String) = {
	  try{
	      authWSService.createSession(lang, "ADMIN_UI")
	  } catch {
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

  def login(session: String, username: String, password: String) = {
	  try{
	      val user = authWSService.login(session, username, password, null)
	      val perm = getCurrentUserPermissions(session)
	      println("from WS:" + perm);
	      val permMap = (Map[Long,Set[PermissionEnum]]()/:perm)((x,y) => x + 
	    		  			{y.getId.longValue -> 
                            (Set[PermissionEnum]()/:y.getPermissions)((a,b) => a + b)})
	      wsSession.set(SessionInfo(session, user, permMap))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getCurrentUserPermissions(session: String) = {
	  try{
	      authWSService.getCurrentUserPermissions(session)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  
  }
}

