package ro.gagarin.snippet

import scala.xml.{NodeSeq, Text, Group, Node}
import scala.collection.mutable.ListBuffer
import net.liftweb.http._
import net.liftweb.http.S
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import net.liftweb.util._
import ro.gagarin.model.adminService
import ro.gagarin.WsUserRole
import ro.gagarin.WsUserPermission

class Roles {
  
  private object selectedRole extends RequestVar[WsUserRole](null)
  
  def list(in: NodeSeq): NodeSeq  = {
      val roles = adminService.getRoleList
      roles.flatMap( u => 
      bind("role", in, 
	      "name" -> link("editRole", () => {selectedRole.set(u)}, Text(u.getRoleName())),
	      "permissions" -> {
		  val permissions = adminService.getRolePermissions(u)
		  Text(("" /: permissions)( (string, perm) => string + " " + perm.getPermissionName()))},
	      "id" -> Text(u.getId().toString)
      ))
  }
    
  def newRole (in: NodeSeq): NodeSeq  = {
	var roleName = "";
    val permissions = adminService.getAllPermissionList.map(x => (x.getId().toString,x.getPermissionName()))
    var permList:List[WsUserPermission] = List[WsUserPermission]();
    bind("role", in, 
         "roleName" -> text("", (x)=> roleName=x),
         "permissions" -> multiSelect(permissions, Seq.empty,(x) => {
           permList = x.map( p => { 
             val perm = new WsUserPermission()
             perm.setId(p.toLong)
             perm
           })
         }) % ("size" -> "10"),
         "submit" -> submit("Create", () => {
           adminService.createRoleWithPermissions(roleName, permList)
           redirectTo("/roles") 
         })
    )
  } 
    

  def editRole (in: NodeSeq): NodeSeq  = {
    val role = selectedRole.is
    val permissions = adminService.getAllPermissionList.map(x => (x.getId().toString,x.getPermissionName()))
    val permList = adminService.getRolePermissions(role).map( x => x.getId().toString);
    var newPermList:List[WsUserPermission] = List[WsUserPermission]();
    bind("role", in, 
         "roleName" -> text(role.getRoleName, (x)=> role.setRoleName(x) ),
         "permissions" -> multiSelect(permissions, permList,(x) => {
           newPermList = x.map( p => { 
             val perm = new WsUserPermission()
             perm.setId(p.toLong)
             perm
           })
         }) % ("size" -> "10"),
         "submit" -> submit("Update", () => {
           adminService.updateRole(role, newPermList)
           redirectTo("/roles") 
         })
    )
  } 
}

