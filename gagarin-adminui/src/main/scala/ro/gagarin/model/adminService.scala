
package ro.gagarin.model

import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.wsclient.WSClient
import _root_.net.liftweb.http.S._
import _root_.ro.gagarin.model.webServiceUtils._
import _root_.scala.collection.mutable.ListBuffer

object adminService {

  
  implicit def convertScalaListToJavaListWsUser(aList:ListBuffer[WsUser]) = java.util.Arrays.asList(aList.toArray: _*)
  implicit def convertScalaListToJavaListWsGroup(aList:ListBuffer[WsGroup]) = java.util.Arrays.asList(aList.toArray: _*)
  implicit def convertScalaListToJavaList(aList:ListBuffer[WsUserPermission]) = java.util.Arrays.asList(aList.toArray: _*)

    def getStatistics(filter: String) = { 
	  try{
	      Buffer(adminWSService.getStatistics(wsSession.session, filter))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

    def getSessionList = { 
	  try{
	      Buffer(adminWSService.getSessionList(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def logoutSession(session: String) = { 
	  try{
	      adminWSService.logoutSession(wsSession.session, session)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getLogEntries(user: String) = {
	  try{
	      Buffer(adminWSService.getLogEntries(wsSession.session, null))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getRolePermissions(role: WsUserRole) = {
	  try{
	      Buffer(adminWSService.getRolePermissions(wsSession.session, role))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getConfigEntries = {
	  try{
	      Buffer(adminWSService.getConfigEntries(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def setConfigEntry(cfg : WsConfig) = {
	  try{
	      adminWSService.setConfigEntry(wsSession.session, cfg)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getUsers = {
	  try{
	      Buffer(adminWSService.getUsers(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getGroups = {
	  try{
	      Buffer(adminWSService.getGroups(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getGroupUsers(group: WsGroup) = {
	  try{
	      Buffer(adminWSService.getGroupUsers(wsSession.session, group))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getRoleList = {
	  try{
	      Buffer(adminWSService.getRoleList(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getAllPermissionList = {
	  try{
	      Buffer(adminWSService.getAllPermissionList(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getControlEntityCategories = {
	  try{
	      Buffer(adminWSService.getControlEntityCategories(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

    def getControlEntityListForCategory(ceCategory: String) = {
	  try{
	      Buffer(adminWSService.getControlEntityListForCategory(wsSession.session,ceCategory))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

    def getPersons = {
	  try{
	      Buffer(adminWSService.getPersons(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
    }

    def getPermissionAssignmentsForControlEntity(ce : WsControlEntity ) = {
	  try{
	      Buffer(adminWSService.getPermissionAssignmentsForControlEntity(wsSession.session, ce))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
    }

    
  def createUser(user : WsUser) = {
	  try{
	      adminWSService.createUser(wsSession.session, user)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def updateUser(user : WsUser) = {
	  try{
	      adminWSService.updateUser(wsSession.session, user)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def createGroup(group : WsGroup) = {
	  try{
	      adminWSService.createGroup(wsSession.session, group)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def updateGroup(group : WsGroup) = {
	try{
	    adminWSService.updateGroup(wsSession.session, group)
	} catch {
	case e: WSException_Exception => {
	    handleException(e)
	}}
  }

  def createRoleWithPermissions(role:String, perm: ListBuffer[WsUserPermission]) = {
	  try{
	      adminWSService.createRoleWithPermissions(wsSession.session, role, perm)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def assignUserToGroup(user: WsUser, group: WsGroup) = {
    val list = new ListBuffer[WsUser]()
    list += user
    try{
	adminWSService.assignUsersToGroup(wsSession.session, group, list)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}
  }

  def unassignUserFromGroup(user: WsUser, group: WsGroup) = {
    val list = new ListBuffer[WsUser]()
    list += user
    try{
	adminWSService.unassignUsersFromGroup(wsSession.session, group, list)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}
  }

  def deleteGroup( group: WsGroup) = {
    try{
	adminWSService.deleteGroup(wsSession.session, group)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}
  }

  def deleteUser( user: WsUser) = {
    try{
	adminWSService.deleteUser(wsSession.session, user)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}
  }
}

