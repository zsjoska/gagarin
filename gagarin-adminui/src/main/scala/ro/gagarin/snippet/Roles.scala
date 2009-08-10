package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.ro.gagarin.wsclient.WSClient
import _root_.ro.gagarin.model.WebServiceClient._
import _root_.ro.gagarin.model.{wsSessionId, SessionInfo}
import _root_.scala.collection.jcl.Buffer

class Roles {
  
  private object selectedRole extends RequestVar[WsUserRole](null)
  
    def list(in: NodeSeq): NodeSeq  = {
   	  val roles = Buffer(getUserService.getRoleList(wsSessionId.session))
      <span>
      <table>
      {roles.flatMap( u => 
        <tr>
          <td>{link("editRole", () => {selectedRole.set(u)}, Text(u.getRoleName()))}</td>
        </tr>)}
      </table>
      </span>
   	  
      
    }
    
  def newRole (in: NodeSeq): NodeSeq  = {
	var roleName = "";
    val permissions = Buffer(getUserService.getAllPermissionList(wsSessionId.session)).map(x => (x.getId().toString,x.getPermissionName()))
    val permList = new java.util.ArrayList[WsUserPermission]();
    bind("role", in, 
         "roleName" -> text("", (x)=> roleName=x),
         "permissions" -> multiSelect(permissions, Seq.empty,(x) => {
           val perm = new WsUserPermission()
           perm.setId(x.toLong)
           permList.add(perm)
         }) % ("size" -> "20"),
         "submit" -> submit("Create", () => {
           getUserService.createRoleWithPermissions(wsSessionId.session, roleName, permList)
           redirectTo("/roles") 
         })
    )
  } 
    

  def editRole (in: NodeSeq): NodeSeq  = {
    val role = selectedRole
    val permissions = Buffer(getUserService.getAllPermissionList(wsSessionId.session)).map(x => (x.getId().toString,x.getPermissionName()))
    val permList = Buffer(getUserService.getRolePermissions(wsSessionId.session, role)).map( x => x.getId().toString);
    bind("role", in, 
         "roleName" -> text(role.getRoleName, (x)=> role.setRoleName(x) ),
         "permissions" -> multiSelect(permissions, permList,(x) => {
         }) % ("size" -> "20"),
         "submit" -> submit("Update", () => {
           // getUserService.createRoleWithPermissions(wsSessionId.session, roleName, permList)
           redirectTo("/roles") 
         })
    )
  } 
}

