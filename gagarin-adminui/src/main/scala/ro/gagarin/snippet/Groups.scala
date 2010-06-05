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

class Groups {
  
  private object selectedGroup extends RequestVar[WsGroup](null)

    def list(in: NodeSeq): NodeSeq  = {
      val groups = adminService.getGroups
      groups.flatMap( u => 
      bind("groups", in, 
	   "edit" -> a(() => Run("$('#dialog-form').dialog('open');"),Text("Edit")),
	   "name" -> link("editGroup", () => {selectedGroup.set(u)}, Text(u.getName())),
	   "description" -> Text(u.getDescription())
      ))
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

    def usersEdit(in: NodeSeq): NodeSeq  = {
      bind("groups", in, 
	   "assignedUsers" -> text("assignedUsers", (x)=> {}),
	   "allUsers" -> text("allUsers", (x) => {})
      )
    }
}

