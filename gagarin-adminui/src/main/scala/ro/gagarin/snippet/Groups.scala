package ro.gagarin.snippet

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
import _root_.net.liftweb.http.js.JsCmds.{Alert, Noop, Replace, SetElemById, Run}
import _root_.net.liftweb.http.js.JsCmd

class Groups {
  
  private object selectedGroup extends RequestVar[WsGroup](null)
  private object dialogMarkup extends SessionVar[NodeSeq](null)
  

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
   */
  def initDisplayDialog(g: WsGroup): JsCmd = {
    Replace("dialog-form", createEditAssignmentsDialog(g))&
    Run("$('#dialog-form').dialog({modal: true});")
  }

  def createEditAssignmentsDialog (g: WsGroup): NodeSeq  = {
      bind("groups", dialogMarkup.is, 
	   "assignedUsers" -> Text(g.getName),
	   "allUsers" -> Text(g.getDescription)
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

