package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.scala.collection.mutable.ListBuffer
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.ro.gagarin.model.userService

class Roles {
  
  private object selectedRole extends RequestVar[WsUserRole](null)
  
    def list(in: NodeSeq): NodeSeq  = {
   	  val roles = userService.getRoleList
      <span>
      <table border="1" cellspacing="0">
      {roles.flatMap( u => 
        <tr>
          <td>{link("editRole", () => {selectedRole.set(u)}, Text(u.getRoleName()))}</td>
        </tr>)}
      </table>
      </span>
   	  
      
    }
    
  def newRole (in: NodeSeq): NodeSeq  = {
	var roleName = "";
    val permissions = userService.getAllPermissionList.map(x => (x.getId().toString,x.getPermissionName()))
    val permList = new ListBuffer[WsUserPermission]();
    bind("role", in, 
         "roleName" -> text("", (x)=> roleName=x),
         "permissions" -> multiSelect(permissions, Seq.empty,(x) => {
           val perm = new WsUserPermission()
           error("Fix the implementation")
//           perm.setId(x.toLong)
//           permList += perm
         }) % ("size" -> "10"),
         "submit" -> submit("Create", () => {
           userService.createRoleWithPermissions(roleName, permList)
           redirectTo("/roles") 
         })
    )
  } 
    

  def editRole (in: NodeSeq): NodeSeq  = {
    val role = selectedRole
    val permissions = userService.getAllPermissionList.map(x => (x.getId().toString,x.getPermissionName()))
    val permList = userService.getRolePermissions(role).map( x => x.getId().toString);
    bind("role", in, 
         "roleName" -> text(role.getRoleName, (x)=> role.setRoleName(x) ),
         "permissions" -> multiSelect(permissions, permList,(x) => {
         }) % ("size" -> "10"),
         "submit" -> submit("Update", () => {
           // getUserService.createRoleWithPermissions(wsSessionId.session, roleName, permList)
           redirectTo("/roles") 
         })
    )
  } 
}

