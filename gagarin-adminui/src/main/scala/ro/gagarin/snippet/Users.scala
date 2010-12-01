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
import _root_.net.liftweb.http.js.JsCmd
import _root_.net.liftweb.http.js.JsCmds.{Alert, Noop, Replace, SetElemById, Run,ReplaceOptions}
import _root_.ro.gagarin.view.TemplateStore
import _root_.scala.collection.jcl.Buffer

class Users {
  
  private object selectedUser extends RequestVar[WsUser](null)
  private object userProperties extends RequestVar[WsPropertySet](null)

  lazy val statusMap = (Map[String,String]()/: UserStatus.values)( (x,y) =>  x + {y.name->y.name}).toSeq;
  lazy val authMap = (Map[String,String]()/: adminService.getAuthenticationTypes)( (x,y) =>  x + {y -> y}).toSeq;
  
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
         "authentication" -> select(authMap, Empty, x => user.setAuthentication(x)),
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
         "authentication" -> select(authMap, Full(user.getAuthentication.name), x => user.setAuthentication(x)),
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
    userProperties.set(properties);
    
    val newField=new WsProperty()

    bind("userProps", TemplateStore.getTemplate("userExtra-form"), 
	 "newName" -> ajaxText("New name", (x) => {
	   newField.setFieldName(x);
	   Noop;
	 }),
	 "newValue" -> ajaxText("New value", (x) => {
	   newField.setFieldValue(x);
	   Noop;
         }),
	 "newAdd" -> ajaxButton("Add", () => {
	   val p = new WsPropertySet
	   p.setId(properties.getId)
	   p.getFields.add(newField);
           adminService.setUserExtra(p);
	   Noop;
	 })
    )
  }
  
  
  def showProperties (in: NodeSeq): NodeSeq  = {
    val properties = userProperties.is
    val fields = Buffer(properties.getFields());
    
    fields.flatMap( f => {
      val rowid = nextFuncName
      bind("property", in,
         AttrBindParam("rowid",rowid,"id"),
         "name" -> Text(f.getFieldName),
         "value" -> Text(f.getFieldValue),
	 "delete" -> a( () => {
	   
	   val p = new WsPropertySet
	   p.setId(properties.getId)
	   val field = new WsProperty
	   field.setFieldName(f.getFieldName())
	   field.setFieldValue(null);
	   p.getFields().add(field);
           adminService.setUserExtra(p);
           
           // $('#myTableRow').remove(); // this could be written somehow with direct jQuery object
           Run("$('#"+rowid+"').remove()")
         }, Text("Delete"))
    	)})
  }
}

