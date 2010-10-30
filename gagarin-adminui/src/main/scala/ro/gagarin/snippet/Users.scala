package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.common.{Full, Empty}
import _root_.ro.gagarin.model.{wsSession, SessionInfo}
import _root_.ro.gagarin.model.adminService
import _root_.ro.gagarin.UserStatus
import _root_.ro.gagarin.AuthenticationType
import _root_.net.liftweb.http.js.JsCmd
import _root_.net.liftweb.http.js.JsCmds.{Alert, Noop, Replace, SetElemById, Run,ReplaceOptions}
import _root_.ro.gagarin.view.TemplateStore

class Users {
  
  private object selectedUser extends RequestVar[WsUser](null)

  lazy val statusMap = (Map[String,String]()/: UserStatus.values)( (x,y) =>  x + {y.name->y.name}).toSeq;
  lazy val authMap = (Map[String,String]()/: AuthenticationType.values)( (x,y) =>  x + {y.name->y.name}).toSeq;
  
    // TODO: put this in a utility
    def __(text: String) = if(text == null) "" else text
  
    def list(in: NodeSeq): NodeSeq  = {
      val users = adminService.getUsers
      users.flatMap( u => 
      	bind("user", in, 
      		"edit" ->link("editUser", () => {selectedUser.set(u)}, Text(u.getUsername())),
      		"delete" ->link("users", () => {
      		    adminService.deleteUser(u)
                        }, Text("Delete")),
      		"name" -> Text(__(u.getName())),
      		"email" -> Text(__(u.getEmail()))
      	))
    }
    
  def newUser (in: NodeSeq): NodeSeq  = {
	val user = new WsUser();
    bind("user", in, 
         "username" -> text("", (x)=> user.setUsername(x)),
         "password" -> password("", (x) => user.setPassword(x)),
         "name" -> text("", (x) => user.setName(x)),
         "email" -> text("", (x) => user.setEmail(x)),
         "phone" -> text("", (x) => user.setPhone(x)),
         "status" -> select( statusMap, Empty, x => user.setStatus(UserStatus.valueOf(x))),
         "authentication" -> select(authMap, Empty, x => user.setAuthentication(AuthenticationType.valueOf(x))),
         "submit" -> submit("Create", () => {
             adminService.createUser(user)
             redirectTo("/users") 
         })
    )
  } 
    

  def editUser (in: NodeSeq): NodeSeq  = {
	val user = selectedUser.is
    bind("user", in, 
         "username" -> text(user.getUsername(), (x)=> user.setUsername(x)),
         "password" -> password("", (x) => user.setPassword(x)),
         "name" -> text( if(user.getName() != null) user.getName() else "", (x) => user.setName(x)),
         "email" -> text( if(user.getEmail()!=null) user.getEmail else "", (x) => user.setEmail(x)),
         "phone" -> text( if(user.getPhone() != null) user.getPhone() else "", (x) => user.setPhone(x)),
         "status" -> select( statusMap, Full(user.getStatus.name), x => user.setStatus(UserStatus.valueOf(x))),
         "authentication" -> select(authMap, Full(user.getAuthentication.name), x => user.setAuthentication(AuthenticationType.valueOf(x))),
         "properties" -> a( () => initDisplayDialog(user), Text("Properties") ),
         "submit" -> submit("Update", () => {
             adminService.updateUser(user);
             redirectTo("/users") 
         })
    	)
  }
  
  /**
   * Creates a javascript command which prepares the dialog box for edit user-group assignments.
   * We have to generate the dialog box content and then invoke the dialog command on it.
   * NOTE: bad thing: we had to hard-code here the width of the dialog
   */
  def initDisplayDialog(u: WsUser): JsCmd = {
    Replace("userExtra-form", createEditPropertiesDialog(u))&
    Run("$('#userExtra-form').dialog({modal: true, width: 700});")
  }

  /**
   * Create the assignments dialog by re-using the saved template
   */
  def createEditPropertiesDialog (u: WsUser): NodeSeq  = {
    var properties = adminService.getUserExtra(u);
    if(properties==null){
      properties = new WsPropertySet
      properties.setId(u.getId);
    }
    
    val field=new WsProperty()
    field.setFieldName("name");
    field.setFieldValue("value");
    properties.getFields.add(field);
    adminService.setUserExtra(properties);
    
    val idAssign = nextFuncName
    val idUnassign = nextFuncName
    
    val idAssignedSelect = nextFuncName
    val idAllUsersSelect = nextFuncName

    bind("userProps", TemplateStore.getTemplate("userExtra-form"), 
	 "test" -> Text("Hello")
    )
  }
  
}

