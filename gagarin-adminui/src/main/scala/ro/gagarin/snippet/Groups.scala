package ro.gagarin.snippet

import _root_.scala.collection.mutable.Map
import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.ro.gagarin.model.{wsSession, SessionInfo}
import _root_.ro.gagarin.model.adminService
import _root_.net.liftweb.http.js.JsCmds.{Alert, Noop, Replace, SetElemById, Run,ReplaceOptions}
import _root_.net.liftweb.http.js.JE.{JsRaw}
import _root_.net.liftweb.http.js.JsCmd
import _root_.net.liftweb.common.{Full, Empty}

class Groups {
  
  private object selectedGroup extends RequestVar[WsGroup](null)
  private object dialogMarkup extends SessionVar[NodeSeq](null)
  private object assignedSelection extends RequestVar[String](null)
  private object allUsersSelection extends RequestVar[String](null)
  

  def list(in: NodeSeq): NodeSeq  = {
      val groups = adminService.getGroups
      groups.flatMap( u => 
      bind("groups", in, 
	   "edit" -> a(() => initDisplayDialog(u),Text("Edit")),
	   "name" -> link("editGroup", () => {selectedGroup.set(u)}, Text(u.getName())),
	   "description" -> Text(u.getDescription())
      ))
  }
  
  /**
   * Creates a javascript command which prepares the dialog box for edit user-group assignments.
   * We have to generate the dialog box content and then invoke the dialog command on it.
   * NOTE: bad thing: we had to hard-code here the width of the dialog
   */
  def initDisplayDialog(g: WsGroup): JsCmd = {
    Replace("dialog-form", createEditAssignmentsDialog(g))&
    Run("$('#dialog-form').dialog({modal: true, width: 700});")
  }
  
  /**
   * Create the assignments dialog by re-using the saved template
   * TODO: similar code for button handling, could be unified
   */
  def createEditAssignmentsDialog (g: WsGroup): NodeSeq  = {
    val users = adminService.getUsers
    val groupUsers = adminService.getGroupUsers(g)

    val groupUsersMap = (Map[String,String]()/: groupUsers)( (x,y) =>  x + {y.getId().toString -> y.getName() });
    val usersMap = (Map[String,String]()/: users)( (x,y) =>  x + {y.getId().toString -> y.getName() });

    // remove from all users the already assigned ones
    groupUsersMap.foreach( x => usersMap -= x.key )
    
    val idAssign = nextFuncName
    val idUnassign = nextFuncName
    
    val idAssignedSelect = nextFuncName
    val idAllUsersSelect = nextFuncName

    bind("groups", dialogMarkup.is, 
	 "assignedUsers" -> ajaxSelect(groupUsersMap.toSeq, Empty,(x) => {
	   assignedSelection.set(x)
	   SetElemById(idUnassign, JsRaw("false"),"disabled")
         }) % ("size" -> "10") % ("id" -> idAssignedSelect),
	 "allUsers" -> ajaxSelect(usersMap.toSeq, Empty,(x) => {
	   allUsersSelection.set(x)
	   SetElemById(idAssign, JsRaw("false"),"disabled")
         }) % ("size" -> "10")  % ("id" -> idAllUsersSelect),
         "assignUser" -> ajaxButton("Assign", () => {
           val toAssignId:String = allUsersSelection.is
           val toAssign:String = usersMap.get(toAssignId).get

           val user = new WsUser();
           user.setId(toAssignId.toLong);
           adminService.assignUserToGroup(user, g);
           
           groupUsersMap += toAssignId -> toAssign
           usersMap -= toAssignId
           
	   SetElemById(idUnassign, JsRaw("true"),"disabled")&
	   SetElemById(idAssign, JsRaw("true"),"disabled")&
           ReplaceOptions(idAssignedSelect, groupUsersMap.toList,Empty)&
           ReplaceOptions(idAllUsersSelect, usersMap.toList,Empty)
         }) % ("disabled" -> "disabled") % ("id" -> idAssign), 
         "unassignUser" -> ajaxButton("Un-Assign", () => {
           val toUnassignId: String = assignedSelection.is
           val toUnassign: String = groupUsersMap.get(toUnassignId).get
           
           val user = new WsUser();
           user.setId(toUnassignId.toLong);
           adminService.unassignUserFromGroup(user, g);

           usersMap += toUnassignId -> toUnassign
           groupUsersMap -= toUnassignId
           
           
	   SetElemById(idUnassign, JsRaw("true"),"disabled")&
	   SetElemById(idAssign, JsRaw("true"),"disabled")&
           ReplaceOptions(idAssignedSelect, groupUsersMap.toList,Empty)&
           ReplaceOptions(idAllUsersSelect, usersMap.toList,Empty)
         }) % ("disabled" -> "disabled") % ("id" -> idUnassign)
    )
  }

  def newGroup (in: NodeSeq): NodeSeq  = {
      val group = new WsGroup();
      bind("group", in, 
	      "groupname" -> text("", (x)=> group.setName(x)),
	      "description" -> text("", (x) => group.setDescription(x)),
	      "submit" -> submit("Create", () => {
		  adminService.createGroup(group)
		  redirectTo("/groups") 
	      })
      )
  } 

  def editGroup (in: NodeSeq): NodeSeq  = {
      val group = selectedGroup.is
      bind("group", in, 
	      "groupname" -> text(group.getName, (x)=> group.setName(x)),
	      "description" -> text(group.getDescription, (x) => group.setDescription(x)),
	      "submit" -> submit("Update", () => {
		  adminService.updateGroup(group)
		  redirectTo("/groups") 
	      })
      )
  } 

  /**
   * We have to create multiple dialog boxes with this template, but we have the template only at the page load.
   * We save here and will reuse every time when we need to open it.
   */
  def usersEdit(in: NodeSeq): NodeSeq  = {
      dialogMarkup.set(in)
      <div id="dialog-form" style="display:none">
        Placeholder
      </div>  
  }
}

