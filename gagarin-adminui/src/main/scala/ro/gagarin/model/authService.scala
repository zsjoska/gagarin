
package ro.gagarin.model

import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.wsclient.WSClient
import _root_.net.liftweb.http.S._
import _root_.ro.gagarin.model.webServiceUtils._
import _root_.scala.collection.mutable.ListBuffer

object authService {
  
  def logout = {
	  try{
		getAuthService.logout(wsSession.session)
		wsSession.set(null)
	  } catch {
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

    def createSession(lang: String) = {
	  try{
	      getAuthService.createSession(lang, "ADMIN_UI")
	  } catch {
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

  def login(session: String, username: String, password: String) = {
	  try{
	      val user = getAuthService.login(session, username, password, null)
	      val perm = getCurrentUserPermissions(session)
	      val permSet = (Set[String]()/:perm)((x,y) => x + y.getPermissionName)
	      wsSession.set(SessionInfo(session,user,permSet))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getCurrentUserPermissions(session: String) = {
	  try{
	      Buffer(getAuthService.getCurrentUserPermissions(session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  
  }
  }

