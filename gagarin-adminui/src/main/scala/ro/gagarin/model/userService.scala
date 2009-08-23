
package ro.gagarin.model

import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.wsclient.WSClient
import _root_.net.liftweb.http.S._
import _root_.ro.gagarin.model.webServiceUtils._
import _root_.scala.collection.mutable.ListBuffer

object userService {
  
  def getLogEntries(user: String) = {
	  try{
		  	Buffer(getUserService.getLogEntries(wsSession.session, null))
	  } catch {
	  case e: OperationException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: PermissionDeniedException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: SessionNotFoundException_Exception => {
	    wsSession.set(null)
	    error(e);
	    redirectTo("/login")
	  }
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

  def getRolePermissions(role: WsUserRole) = {
	  try{
		  	Buffer(getUserService.getRolePermissions(wsSession.session, role))
	  } catch {
	  case e: OperationException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: PermissionDeniedException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: SessionNotFoundException_Exception => {
	    wsSession.set(null)
	    error(e);
	    redirectTo("/login")
	  }
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }
  
  def getConfigEntries = {
	  try{
		  	Buffer(getUserService.getConfigEntries(wsSession.session))
	  } catch {
	  case e: OperationException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: PermissionDeniedException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: SessionNotFoundException_Exception => {
	    wsSession.set(null)
	    error(e);
	    redirectTo("/login")
	  }
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

  def setConfigEntry(cfg : WsConfig) = {
	  try{
		  	getUserService.setConfigEntry(wsSession.session, cfg)
	  } catch {
	  case e: OperationException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: PermissionDeniedException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: SessionNotFoundException_Exception => {
	    wsSession.set(null)
	    error(e);
	    redirectTo("/login")
	  }
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

  def getUsers = {
	  try{
		  	Buffer(getUserService.getUsers(wsSession.session))
	  } catch {
	  case e: OperationException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: PermissionDeniedException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: SessionNotFoundException_Exception => {
	    wsSession.set(null)
	    error(e);
	    redirectTo("/login")
	  }
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

  def getRoleList = {
	  try{
		  	Buffer(getUserService.getRoleList(wsSession.session))
	  } catch {
	  case e: OperationException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: PermissionDeniedException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: SessionNotFoundException_Exception => {
	    wsSession.set(null)
	    error(e);
	    redirectTo("/login")
	  }
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

  def getAllPermissionList = {
	  try{
		  	Buffer(getUserService.getAllPermissionList(wsSession.session))
	  } catch {
	  case e: OperationException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: PermissionDeniedException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: SessionNotFoundException_Exception => {
	    wsSession.set(null)
	    error(e);
	    redirectTo("/login")
	  }
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }
  
  def createUser(user : WsUser) = {
	  try{
		  	getUserService.createUser(wsSession.session, user)
	  } catch {
	  case e: OperationException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: PermissionDeniedException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: SessionNotFoundException_Exception => {
	    wsSession.set(null)
	    error(e);
	    redirectTo("/login")
	  }
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }

  implicit def convertScalaListToJavaList(aList:ListBuffer[WsUserPermission]) = java.util.Arrays.asList(aList.toArray: _*)
  
  def createRoleWithPermissions(role:String, perm: ListBuffer[WsUserPermission]) = {
	  try{
		  	getUserService.createRoleWithPermissions(wsSession.session, role, perm)
	  } catch {
	  case e: OperationException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: PermissionDeniedException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: DataConstraintException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: ItemNotFoundException_Exception => {
	    error(e);
		redirectTo("/")
	  }
	  case e: SessionNotFoundException_Exception => {
	    wsSession.set(null)
	    error(e);
	    redirectTo("/login")
	  }
	  case e: Exception => {
	    error("Unexpected exception occured: " + e.getMessage());
	    redirectTo("/")
	  }}
  }
}

