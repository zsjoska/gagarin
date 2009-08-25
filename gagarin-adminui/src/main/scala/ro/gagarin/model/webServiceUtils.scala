package ro.gagarin.model

import _root_.ro.gagarin.wsclient.WSClient

object webServiceUtils {
  
  // TODO: move this URL to config file
  
  val wsclient = WSClient.getWSClient("http://localhost:8080/gagarin-ws/ws/")
  
  def getAuthService = wsclient.getAuthentication()
  def getUserService = wsclient.getUserService()
  
  implicit def errorFromException(e : OperationException_Exception) = {
	  val fi = e.getFaultInfo
	  "Error " + fi.getErrorCode + ": " + fi.getMessage + "; " + fi.getDetail 
  }
  implicit def errorFromException(e : PermissionDeniedException_Exception) = {
	  val fi = e.getFaultInfo
	  "Permission denied for " + fi.getUsername + ": " + fi.getName  
  }
  implicit def errorFromException(e : SessionNotFoundException_Exception) = 
	"Your session expired. please log-in again."
  implicit def errorFromException(e : DataConstraintException_Exception) = {
	  val fi = e.getFaultInfo
	  "Could not create record " + fi.getMessage + ": " + fi.getDetail  
  }
  implicit def errorFromException(e : ItemNotFoundException_Exception) = {
	  val fi = e.getFaultInfo
	  "A required item was not found " + fi.getMessage + ": " + fi.getDetail  
  }
  implicit def errorFromException(e : LoginRequiredException_Exception) = {
	  val fi = e.getFaultInfo
	  "Login required." 
  }
  
}

