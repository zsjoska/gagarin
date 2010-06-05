
package ro.gagarin.model

import _root_.scala.collection.jcl.Buffer
import _root_.ro.gagarin.wsclient.WSClient
import _root_.net.liftweb.http.S._
import _root_.ro.gagarin.model.webServiceUtils._
import _root_.scala.collection.mutable.ListBuffer

object adminService {
  
    def getStatistics(filter: String) = { 
	  try{
	      Buffer(getUserService.getStatistics(wsSession.session, filter))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

    def getSessionList = { 
	  try{
	      Buffer(getUserService.getSessionList(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def logoutSession(session: String) = { 
	  try{
	      getUserService.logoutSession(wsSession.session, session)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getLogEntries(user: String) = {
	  try{
	      Buffer(getUserService.getLogEntries(wsSession.session, null))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getRolePermissions(role: WsUserRole) = {
	  try{
	      Buffer(getUserService.getRolePermissions(wsSession.session, role))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getConfigEntries = {
	  try{
	      Buffer(getUserService.getConfigEntries(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def setConfigEntry(cfg : WsConfig) = {
	  try{
	      getUserService.setConfigEntry(wsSession.session, cfg)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getUsers = {
	  try{
	      Buffer(getUserService.getUsers(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getGroups = {
	  try{
	      Buffer(getUserService.getGroups(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getGroupUsers(group: WsGroup) = {
	  try{
	      Buffer(getUserService.getGroupUsers(wsSession.session, group))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getRoleList = {
	  try{
	      Buffer(getUserService.getRoleList(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getAllPermissionList = {
	  try{
	      Buffer(getUserService.getAllPermissionList(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getControlEntityCategories = {
	  try{
	      Buffer(getUserService.getControlEntityCategories(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

    def getControlEntityListForCategory(ceCategory: String) = {
	  try{
	      Buffer(getUserService.getControlEntityListForCategory(wsSession.session,ceCategory))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

    def getPersons = {
	  try{
	      Buffer(getUserService.getPersons(wsSession.session))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
    }

    def getPermissionAssignmentsForControlEntity(ce : WsControlEntity ) = {
	  try{
	      Buffer(getUserService.getPermissionAssignmentsForControlEntity(wsSession.session, ce))
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
    }

    
  def createUser(user : WsUser) = {
	  try{
	      getUserService.createUser(wsSession.session, user)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def updateUser(user : WsUser) = {
	  try{
	      getUserService.updateUser(wsSession.session, user)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def createGroup(group : WsGroup) = {
	  try{
	      getUserService.createGroup(wsSession.session, group)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def updateGroup(group : WsGroup) = {
	try{
	    getUserService.updateGroup(wsSession.session, group)
	} catch {
	case e: WSException_Exception => {
	    handleException(e)
	}}
  }

  implicit def convertScalaListToJavaList(aList:ListBuffer[WsUserPermission]) = java.util.Arrays.asList(aList.toArray: _*)
  
  def createRoleWithPermissions(role:String, perm: ListBuffer[WsUserPermission]) = {
	  try{
	      getUserService.createRoleWithPermissions(wsSession.session, role, perm)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }
}

