package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text,Unparsed, Group, Node}
import _root_.scala.xml.NodeSeq._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.ro.gagarin.model.{wsSession, SessionInfo}
import _root_.ro.gagarin.model.adminService
import _root_.net.liftweb.common.{Full, Empty}
import _root_.net.liftweb.http.js.JsCmd
import _root_.net.liftweb.http.js.JsCmds.{Alert, Noop, Replace, SetElemById}
import _root_.net.liftweb.http.js.JE.{JsRaw}

class Permissions {
  
    private object selectedCategory extends RequestVar[ControlEntityCategory](null)
    private object selCId extends RequestVar[String](null)
    private object selPId extends RequestVar[String](null)

  
    def listCategories(in: NodeSeq): NodeSeq  = {
	val categories = adminService.getControlEntityCategories
        <ul>
        {
          categories.flatMap( u =>
            <li>{link("permissionPage", () => {selectedCategory.set(u)}, <span>{u.name}</span>)}</li>
        )}
        </ul>
    }
      
    def pageTitle(in: NodeSeq): NodeSeq  = {
        val cat = selectedCategory.is
        Text(cat name)
    }
    
    // TODO: move this markup to html
    def existingAssignments(in: NodeSeq): NodeSeq  = {
        val cat = selectedCategory.is
        val div = "exAssignmentsDiv" + cat.name;
        val table = "exAssignmentsTable" + cat.name;
    	<div id={div} style="width: 100%; display:none">Existing assignments for { cat.name }<br/>
	<div id={table}>Placeholder</div>
	</div>
    }
    
    // TODO: move this markup to html
    def blankEffectivePermissions(in: NodeSeq): NodeSeq  = {
        val cat = selectedCategory.is
        val divOut = "effectivePermDivOuter" + cat.name;
        val divIn = "effectivePermDivInner" + cat.name;
    	<div id={divOut} style="width: 100%; display:none">
	<div id={divIn}>Placeholder</div>
	</div>
    }

    // TODO: move this markup to html
    def listObjectAssignments(catId: String): NodeSeq  = {
        val cat = selectedCategory.is
        val ce = new WsControlEntity();
        ce.setId(catId.toLong);
        val list = adminService.getPermissionAssignmentsForControlEntity(ce)
        <div id={"exAssignmentsTable" + cat.name}>
          {if(list.size==0) "No assignments"}
          <table border="1" cellspacing="0" cellpadding="4">
          {list.flatMap( u => 
            <tr>
            <th>Owner</th>
            <th>Role</th>
            </tr>
            <tr>
            <td>{ Text(u.getOwner().getTitle())}</td>
            <td>{Text(u.getRole().getRoleName())}</td>
            </tr>)}
          </table>
        </div>
    }

    def listControlObjects(in: NodeSeq): NodeSeq  = {
        val cat = selectedCategory.is
        val objects = adminService.getControlEntityListForCategory(cat.name)
        val ceMap = (Map[String,String]()/: objects)( (x,y) =>  x + {y.getId().toString -> y.getName() }).toSeq;
        ajaxSelect( ceMap, Empty, x => {
          selCId.set(x)
          updatePage
        }) % ("size" -> "10")
    }
    
    def listOwners(in: NodeSeq): NodeSeq  = {
        val owners = adminService.getOwners
        val ownerMap = (Map[String,String]()/: owners)( (x,y) =>  x + {y.getId().toString -> y.getTitle() }).toSeq;
        ajaxSelect( ownerMap, Empty, x => {
          selPId.set(x)
          updatePage
        }) % ("size" -> "10")
    }

    def updatePage: JsCmd =  updateAssignmentTable & updateEffectivePerms
    
    // TODO: move this markup to html
    def updateEffectivePerms : JsCmd = {
      val cat = selectedCategory.is
      val selCategory = selCId.is
      val selOwner = selPId.is
      if(selCId.is != null && selPId.is != null){
        
        val ce = new WsControlEntity();
        ce.setId(selCategory.toLong)
        
      	val pe = new WsOwner();
        pe.setId(selOwner.toLong)
        
        val permissions = adminService.getEffectivePermissionsObjectOwner(ce, pe)
        val display = ("" /: permissions )( (x,y) => x + "<p>"+ y.name +"</p>")
        Replace("effectivePermDivInner" + cat.name, 
                <div id={"effectivePermDivInner" + cat.name}>
                  {Unparsed(display)}
                </div>)&
        SetElemById("effectivePermDivOuter" + cat.name, JsRaw("'block'"),"style", "display")
      } else 
          Noop
    }
    
    def updateAssignmentTable : JsCmd = {
      val cat = selectedCategory.is
      if(selCId.is != null)
          Replace("exAssignmentsTable" + cat.name, listObjectAssignments(selCId.is))&
          SetElemById("exAssignmentsDiv" + cat.name, JsRaw("'block'"),"style", "display")
      else 
          Noop
    }
    
    def listRoles(in: NodeSeq): NodeSeq  = {
        val roles = adminService.getRoleList
        val roleMap = (Map[String,String]()/: roles)( (x,y) =>  x + {y.getId().toString -> y.getRoleName() }).toSeq;
        ajaxSelect( roleMap, Empty, x => Noop) % ("size" -> "10")
    }
}

