package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.http.LiftServlet
import _root_.ro.gagarin.model.{wsSession, SessionInfo}
import ro.gagarin.model.PermissionHelper._
import _root_.ro.gagarin.PermissionEnum
import _root_.ro.gagarin.PermissionEnum._
import _root_.net.liftweb.http.ResourceServer
import _root_.net.liftweb.http.S._
import ro.gagarin.model.CommonCe._
import ro.gagarin.model.CE

/**
  * A class that's instantiated early and run.  It allows the application
  * to modify lift's environment
  */
class Boot {
  def boot {
    
    // sending XML prologue doesn't likes the jQuery-ui tabs widget
    LiftRules.useXhtmlMimeType = false    
    
    ResourceServer.allow({
      case "3rd-party-js" :: _ => true
    })     
    
    // where to search snippet
    LiftRules.addToPackages("ro.gagarin")

    // check for login on the pages
    def requiresLogin = If(()=>wsSession.is!=null, () => RedirectResponse("/login"))
    def loggedIn = If(()=>wsSession.is==null, () => RedirectResponse("/"))
    def rqPerm(ce: CE, p: PermissionEnum) = If(()=>{ hasPermission( ce, p)}, () => RedirectResponse("/"))
    
    // Build SiteMap
    val entries = SiteMap( 
      Menu(Loc("test", List("test"), "Test", loggedIn)),
      Menu(Loc("Home", List("index"), "Home", requiresLogin)),
      Menu(Loc("login", List("login"), "Login", loggedIn)),
      Menu(Loc("users", List("users"), "Users", requiresLogin, rqPerm( USER_CE, LIST)),
	      Menu(Loc("newUser", List("newUser"), "New User", requiresLogin, rqPerm(USER_CE, CREATE))),
	      Menu(Loc("editUser", List("editUser"), "Edit User", Hidden, requiresLogin, rqPerm(USER_CE, UPDATE)))
      ),
      Menu(Loc("roles", List("roles"), "Roles", requiresLogin, rqPerm(ADMIN_CE, LIST)),
	      Menu(Loc("newRole", List("newRole"), "New Role", requiresLogin, rqPerm(ADMIN_CE, CREATE))),
	      Menu(Loc("editRole", List("editRole"), "Edit Role", Hidden, requiresLogin, rqPerm(ADMIN_CE, UPDATE)))
      ),
      Menu(Loc("groups", List("groups"), "Groups", requiresLogin, rqPerm( GROUP_CE, LIST)),
	      Menu(Loc("newGroup", List("newGroup"), "New Group", requiresLogin, rqPerm( GROUP_CE, CREATE))),
	      Menu(Loc("editGroup", List("editGroup"), "Edit Group", Hidden, requiresLogin, rqPerm(GROUP_CE, UPDATE)))
      ),
      Menu(Loc("permissions", List("permissions"), "Permissions", requiresLogin, rqPerm(PERMISSION_CE, LIST)),
	      Menu(Loc("permissionPage", List("permissionPage"), "permissionPage", Hidden, requiresLogin, rqPerm(PERMISSION_CE, LIST)))
      ),
      Menu(Loc("monitor", List("monitor"), "Monitor", requiresLogin, rqPerm(ADMIN_CE, ADMIN)),
	      Menu(Loc("sessions", List("sessions"), "Sessions", requiresLogin, rqPerm(ADMIN_CE, ADMIN))),
	      Menu(Loc("statistics", List("statistics"), "Statistics", requiresLogin, rqPerm(ADMIN_CE, ADMIN))),
	      Menu(Loc("logs", List("logs"), "Logs", requiresLogin, rqPerm(ADMIN_CE, ADMIN)))
      ),
      Menu(Loc("config", List("config"), "Config", requiresLogin, rqPerm(ADMIN_CE, ADMIN)))
    )
    
    LiftRules.setSiteMap(entries)
  }
}

