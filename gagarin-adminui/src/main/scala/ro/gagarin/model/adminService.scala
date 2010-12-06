
package ro.gagarin.model

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Buffer

import net.liftweb.http.S._

import ro.gagarin.wsclient.WSClient
import ro.gagarin.model.webServiceUtils._

import ro.gagarin._

object adminService {

  
  implicit def convertScalaListToJavaListWsUser(aList:ListBuffer[WsUser]) = java.util.Arrays.asList(aList.toArray: _*)
  implicit def convertScalaListToJavaListWsGroup(aList:ListBuffer[WsGroup]) = java.util.Arrays.asList(aList.toArray: _*)
  implicit def convertScalaListToJavaList(aList:ListBuffer[WsUserPermission]) = java.util.Arrays.asList(aList.toArray: _*)
  implicit def convertScalaListToJavaList(aList:List[WsUserPermission]) = java.util.Arrays.asList(aList.toArray: _*)

    def getStatistics(filter: String):Buffer[WsStatistic] = { 
	  try{
	      adminWSService.getStatistics(wsSession.session, filter)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

    def getSessionList: Buffer[WsExportedSession] = { 
	  try{
	      adminWSService.getSessionList(wsSession.session)
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

  def getLogEntries(user: String): Buffer[WsLogEntry] = {
	  try{
	      adminWSService.getLogEntries(wsSession.session, null)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getRolePermissions(role: WsUserRole): Buffer[WsUserPermission] = {
	  try{
	      adminWSService.getRolePermissions(wsSession.session, role)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getConfigEntries: Buffer[WsConfig] = {
	  try{
	      adminWSService.getConfigEntries(wsSession.session)
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

  def getUsers: Buffer[WsUser] = {
	  try{
	      adminWSService.getUsers(wsSession.session)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getGroups: Buffer[WsGroup] = {
	  try{
	      adminWSService.getGroups(wsSession.session)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getGroupUsers(group: WsGroup): Buffer[WsUser] = {
	  try{
	      adminWSService.getGroupUsers(wsSession.session, group)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getRoleList: Buffer[WsUserRole] = {
	  try{
	      adminWSService.getRoleList(wsSession.session)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getAllPermissionList: Buffer[WsUserPermission] = {
	  try{
	      adminWSService.getAllPermissionList(wsSession.session)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def getControlEntityCategories: Buffer[ControlEntityCategory] = {
	  try{
	      adminWSService.getControlEntityCategories(wsSession.session)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

    def getControlEntityListForCategory(ceCategory: String):Buffer[WsControlEntity] = {
	  try{
	      adminWSService.getControlEntityListForCategory(wsSession.session,ceCategory)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

    def getOwners: Buffer[WsOwner] = {
	  try{
	      adminWSService.getOwners(wsSession.session)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
    }

    def getPermissionAssignmentsForControlEntity(ce : WsControlEntity ):Buffer[WsPermOwnerCEAssignment] = {
	  try{
	      adminWSService.getPermissionAssignmentsForControlEntity(wsSession.session, ce)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
    }

    def getEffectivePermissionsObjectOwner(ce : WsControlEntity, owner: WsOwner ): Buffer[PermissionEnum] = {
	  try{
	      adminWSService.getEffectivePermissionsObjectOwner(wsSession.session, ce, owner)
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

  def createRoleWithPermissions(role:String, perm: List[WsUserPermission]) = {
	  try{
	      adminWSService.createRoleWithPermissions(wsSession.session, role, perm)
	  } catch {
	  case e: WSException_Exception => {
	    handleException(e)
	  }}
  }

  def updateRole(role:WsUserRole, perm: List[WsUserPermission]) = {
	  try{
	      adminWSService.updateRole(wsSession.session, role, perm)
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

  def assignRoleToControlEntity(ce: WsControlEntity, role: WsUserRole,  owner: WsOwner) = {
    try{
	adminWSService.assignRoleToControlEntity(wsSession.session, ce, role, owner)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}
  }

  def unAssignRoleFromControlEntity(ce: WsControlEntity, role: WsUserRole,  owner: WsOwner) = {
    try{
	adminWSService.unAssignRoleFromControlEntity(wsSession.session, ce, role, owner)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}
  }

  def setUserExtra(userExtra: WsPropertySet) = {
    try{
	adminWSService.setUserExtra(wsSession.session, userExtra)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}
  }
  
  def getUserExtra(user: WsUser):WsPropertySet = {
    try{
	adminWSService.getUserExtra(wsSession.session, user)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}
  }

  def getAuthenticationTypes: Buffer[String] = {
    try{
        adminWSService.getAuthenticationTypes(wsSession.session)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}
    }

  def getJobs: Buffer[WsJob] = {
    try{
        adminWSService.getServerJobs(wsSession.session)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}}

  def getThreads: Buffer[WsThread] = {
    try{
        adminWSService.getServerThreads(wsSession.session)
    } catch {
    case e: WSException_Exception => {
	handleException(e)
    }}}
}

