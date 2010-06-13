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
import _root_.ro.gagarin.model.adminService

class Roles {
  
  private object selectedRole extends RequestVar[WsUserRole](null)
  
  def list(in: NodeSeq): NodeSeq  = {
      val roles = adminService.getRoleList
      roles.flatMap( u => 
      bind("role", in, 
	      "name" -> link("editRole", () => {selectedRole.set(u)}, Text(u.getRoleName()))
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

