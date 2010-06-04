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
import _root_.ro.gagarin.model.userService

class Groups {
  
  private object selectedGroup extends RequestVar[WsGroup](null)
  
    def list(in: NodeSeq): NodeSeq  = {
      val groups = userService.getGroups
      <span>
      <table border="1" cellspacing="0" cellpadding="4">
      <tr>
      <th>Group Name</th>
      <th>Description</th>
      </tr>
      {groups.flatMap( u => 
        <tr>
          <td>{link("editGroup", () => {selectedGroup.set(u)}, Text(u.getName()))}</td>
          <td>{Text(u.getDescription())}</td>
        </tr>)}
      </table>
      </span>
    }
    
  def newGroup (in: NodeSeq): NodeSeq  = {
      val group = new WsGroup();
      bind("group", in, 
	      "groupname" -> text("", (x)=> group.setName(x)),
	      "description" -> text("", (x) => group.setDescription(x)),
	      "submit" -> submit("Create", () => {
		  userService.createGroup(group)
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
		  userService.updateGroup(group)
		  redirectTo("/groups") 
	      })
      )
  } 
}

