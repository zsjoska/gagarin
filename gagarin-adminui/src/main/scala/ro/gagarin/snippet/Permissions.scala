package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text,Unparsed, Group, Node}
import _root_.scala.xml.NodeSeq._
import _root_.net.liftweb.http.{RequestVar, SessionVar}
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.ro.gagarin.model.{wsSession, SessionInfo}
import _root_.ro.gagarin.model.adminService
import _root_.net.liftweb.common.{Full, Empty}
import _root_.net.liftweb.http.js.JsCmd
import _root_.net.liftweb.http.js.JsCmds.{Alert, Noop, Replace, SetElemById}
import _root_.net.liftweb.http.js.JE.{JsRaw}

import _root_.ro.gagarin.view.TemplateStore

class Permissions {
  
  val EXISTING_ASSIGNMENTS = "existing-assignments"
  val EFFECTIVE_PERMISSIONS = "effective-permissions"
  val ASSIGN_BUTTON = "assign-button"
  
  private object selectedCategory extends RequestVar[ControlEntityCategory](null)
  private object selCId extends RequestVar[String](null)
  private object selPId extends RequestVar[String](null)
  private object selRId extends RequestVar[String](null)
  private object ceMap extends SessionVar[Map[String,String]](Map.empty)

  def listCategories(in: NodeSeq): NodeSeq  = {
    val categories = adminService.getControlEntityCategories
    categories.flatMap( u =>  bind("category", in,
           "name" -> link("permissionPage", () => {selectedCategory.set(u)}, <span>{u.name}</span>)
      ))
  }
      
  def pageTitle(in: NodeSeq): NodeSeq  = {
    val cat = selectedCategory.is
    Text(cat name)
  }
    
  /**
   * Replaces the lift:permissions.storeAndReplaceAssignments tag 
   * with a placeholder and stores it's content with the id key.
   * This is a customization for the standard lift:templateMgr.storeAndReplace
   * by adding a customized id value.
   */
   def storeAndReplaceAssignments(in: NodeSeq): NodeSeq  = {
     val id:String = EXISTING_ASSIGNMENTS + selectedCategory.is
     TemplateStore.storeReplaceElem(in) % ("id" -> id)
   }

  /**
   * Replaces the lift:permissions.storeAndReplaceEffPermissions tag 
   * with a placeholder and stores it's content with the id key.
   * This is a customization for the standard lift:templateMgr.storeAndReplace
   * by adding a customized id value.
   */
   def storeAndReplaceEffPermissions(in: NodeSeq): NodeSeq  = {
     val id:String = EFFECTIVE_PERMISSIONS + selectedCategory.is
     TemplateStore.storeReplaceElem(in) % ("id" -> id)
   }

  /**
   * Creates the markup to be returned by JS to display the existing assignments 
   */
   def listObjectAssignments(): NodeSeq  = {
     val cat = selectedCategory.is
     bind("assignments", TemplateStore.getTemplate(EXISTING_ASSIGNMENTS, cat.name),
          "object" -> ceMap.get(selCId.is),
          "category" -> selectedCategory.is.name(),
     )
   }

  /**
   * Generates a list of effective permissions an owner has over a ControlEntity
   */
   def effectivePermList(in: NodeSeq): NodeSeq  = {
     val cat = selectedCategory.is
     val selCategory = selCId.is
     val selOwner = selPId.is
     
     val ce = new WsControlEntity();
     ce.setId(selCategory.toLong)
       
     val pe = new WsOwner();
     pe.setId(selOwner.toLong)
        
     val permissions = adminService.getEffectivePermissionsObjectOwner(ce, pe)
     
     permissions.flatMap( u =>  bind("effectivePerms", in,
          "permName" -> u.name )
     )
   }
   
  /**
   * Generates the list of existing permission assignments for the selected control entity 
   */
   def assignmentsTable(in: NodeSeq): NodeSeq  = {
     val ce = new WsControlEntity();
     ce.setId(selCId.is.toLong);
     val list = adminService.getPermissionAssignmentsForControlEntity(ce)
     list.flatMap( u =>  bind("assignment", in,
	     "ce" -> ceMap.get(selCId.is),
	     "owner" -> u.getOwner().getTitle(),
	     "role" -> u.getRole().getRoleName(),
	     "deleteLink" -> a(() => {
               val ce = new WsControlEntity
               val role = new WsUserRole
               val owner = new WsOwner  
               ce.setId( selCId.is.toLong )
               ce.setCategory( selectedCategory.is )
               role.setId( u.getRole().getId )
               owner.setId( u.getOwner().getId )
               println("selCId.is.toLong=" + selCId.is + " u.getOwner().getId=" + u.getOwner().getId + " u.getRole().getId=" + u.getRole().getId)
               adminService.unAssignRoleFromControlEntity(ce, role, owner);
	       updatePage
	     },Text("Delete")),
     ))
   }

  /**
   * Generates a SELECT box with the control entities for a category defined in the system  
   */
  def listControlObjects(in: NodeSeq): NodeSeq  = {
    val cat = selectedCategory.is
    val objects = adminService.getControlEntityListForCategory(cat.name)
    val ceObjMap = (Map[String,String]()/: objects)( (x,y) =>  x + {y.getId().toString -> y.getName() });
    val ceSeq = ceObjMap.toSeq;
    ceMap(ceObjMap);
    ajaxSelect( ceSeq, Empty, x => {
	selCId.set(x)
	updatePage
    }) % ("size" -> "10")
  }
    
  /**
   * Generates a SELECT box with the existing owners in the system 
   */
  def listOwners(in: NodeSeq): NodeSeq  = {
    val owners = adminService.getOwners
    val ownerMap = (Map[String,String]()/: owners)( (x,y) =>  x + {y.getId().toString -> y.getTitle() }).toSeq;
    ajaxSelect( ownerMap, Empty, x => {
      selPId.set(x)
	updatePage
    }) % ("size" -> "10")
  }

  /**
   * Page updating scrips 
   */
  def updatePage: JsCmd =  updateAssignmentTable & updateEffectivePerms & updateAssignButton
    
  /**
   * Creates a JS for updating the effective permissions on the page 
   */
  def updateEffectivePerms : JsCmd = {
    val cat = selectedCategory.is
    val selCategory = selCId.is
    val selOwner = selPId.is
    if(selCId.is != null && selPId.is != null){
      Replace(EFFECTIVE_PERMISSIONS + cat.name, TemplateStore.getTemplate(EFFECTIVE_PERMISSIONS, cat.name) ) &
      SetElemById(EFFECTIVE_PERMISSIONS + cat.name, JsRaw("'block'"),"style", "display")
    } else
      Noop
  }
    
  /**
   * Updates the existing assignments page section 
   */
  def updateAssignmentTable : JsCmd = {
    val cat = selectedCategory.is
    if(selCId.is != null)
	Replace( EXISTING_ASSIGNMENTS + cat.name, listObjectAssignments())&
	SetElemById(EXISTING_ASSIGNMENTS + cat.name, JsRaw("'block'"),"style", "display")
    else
      Noop
  }
    
  /**
   * Updates the assign button availability 
   */
  def updateAssignButton : JsCmd = {
    val disabled = selRId.is == null ||  selCId.is == null ||  selPId.is == null
    val id = ASSIGN_BUTTON + selectedCategory.is.name
    SetElemById(id, JsRaw(disabled.toString),"disabled")
  }
    
    
  /**
   * Creates a SELECT box with the existing roles 
   */
  def listRoles(in: NodeSeq): NodeSeq  = {
    val roles = adminService.getRoleList
    val roleMap = (Map[String,String]()/: roles)( (x,y) =>  x + {y.getId().toString -> y.getRoleName() }).toSeq;
      ajaxSelect( roleMap, Empty, x => {
          selRId.set(x)
          updatePage
      }) % ("size" -> "10")
  }

  /**
   * Handles the event when the Assign button is clicked 
   */
  def assign(in: NodeSeq): NodeSeq  = {
    val id = ASSIGN_BUTTON + selectedCategory.is.name
    ajaxButton(in, ()=> {
      val ce = new WsControlEntity
      val role = new WsUserRole
      val owner = new WsOwner  
      ce.setId( selCId.is.toLong )
      ce.setCategory( selectedCategory.is )
      role.setId( selRId.is.toLong )
      owner.setId( selPId.is.toLong )
      adminService.assignRoleToControlEntity(ce, role, owner);
      updatePage
    }) % ("disabled" -> "true") % ("id"-> id)
  }
}
                                                                                                                                            