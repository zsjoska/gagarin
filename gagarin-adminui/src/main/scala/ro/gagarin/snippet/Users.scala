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

class Users {
  
  private object selectedUser extends RequestVar[WsUser](null)
  
    def list(in: NodeSeq): NodeSeq  = {
   	  val users = userService.getUsers
      <span>
      <table border="1" cellspacing="0">
      {users.flatMap( u => <tr>
                      		<td>{link("editUser", () => {selectedUser.set(u)}, Text(u.getUsername()))}</td>
                      		<td>{Text(u.getName())}</td>
                      	   </tr>)}
      </table>
      </span>
   	  
      
    }
    
  def newUser (in: NodeSeq): NodeSeq  = {
	val user = new WsUser();
    bind("user", in, 
         "username" -> text("", (x)=> user.setUsername(x)),
         "password" -> password("", (x) => user.setPassword(x)),
         "name" -> text("", (x) => user.setName(x)),
         "email" -> text("", (x) => user.setEmail(x)),
         "phone" -> text("", (x) => user.setPhone(x)),
         "submit" -> submit("Create", () => {
        	 	userService.createUser(user)
                redirectTo("/users") 
              })
    )
  } 
    

  def editUser (in: NodeSeq): NodeSeq  = {
	val user = selectedUser
    bind("user", in, 
         "username" -> text(user.getUsername(), (x)=> user.setUsername(x)),
         "password" -> password("", (x) => user.setPassword(x)),
         "name" -> text( if(user.getName() != null) user.getName() else "", (x) => user.setName(x)),
         "email" -> text( if(user.getEmail()!=null) user.getEmail else "", (x) => user.setEmail(x)),
         "phone" -> text( if(user.getPhone() != null) user.getPhone() else "", (x) => user.setPhone(x)),
         "submit" -> submit("Update", () => {
        	 	// getUserService.createUser(wsSessionId.session, user)
                redirectTo("/users") 
              })
    )
  } 
}

