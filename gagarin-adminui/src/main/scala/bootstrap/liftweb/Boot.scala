package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.ro.gagarin.model.{wsSession, SessionInfo}

/**
  * A class that's instantiated early and run.  It allows the application
  * to modify lift's environment
  */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("ro.gagarin")

    // check for login on the pages
    def requiresLogin = If(()=>wsSession.is!=null, () => RedirectResponse("/login"))
    def loggedIn = If(()=>wsSession.is==null, () => RedirectResponse("/"))
    def rqPerm(p:String) = If(()=>wsSession.permSet.contains(p) , () => RedirectResponse("/"))
    
    // Build SiteMap
    val entries = SiteMap( Menu(Loc("Home", List("index"), "Home", requiresLogin)),
    					   Menu(Loc("login", List("login"), "Login", loggedIn)),
    					   Menu(Loc("users", List("users"), "Users", requiresLogin, rqPerm("LIST_USERS")),
	    					   Menu(Loc("newUser", List("newUser"), "New User", requiresLogin, rqPerm("CREATE_USER"))),
	    					   Menu(Loc("editUser", List("editUser"), "Edit User", Hidden, requiresLogin, rqPerm("UPDATE_USER")))
    					   ),
    					   Menu(Loc("roles", List("roles"), "Roles", requiresLogin, rqPerm("LIST_ROLES")),
	    					   Menu(Loc("newRole", List("newRole"), "New Role", requiresLogin, rqPerm("CREATE_ROLE"))),
	    					   Menu(Loc("editRole", List("editRole"), "Edit Role", Hidden, requiresLogin, rqPerm("UPDATE_ROLE")))
    					   ),
    					   Menu(Loc("monitor", List("monitor"), "Monitor", requiresLogin, rqPerm("ADMIN_OPERATION")),
	    					   Menu(Loc("sessions", List("sessions"), "Sessions", requiresLogin, rqPerm("ADMIN_OPERATION"))),
	    					   Menu(Loc("statistics", List("statistics"), "Statistics", requiresLogin, rqPerm("ADMIN_OPERATION"))),
	    					   Menu(Loc("logs", List("logs"), "Logs", requiresLogin, rqPerm("ADMIN_OPERATION")))
    					   ),
    					   Menu(Loc("config", List("config"), "Config", requiresLogin, rqPerm("ADMIN_OPERATION")))
    					   
    					)
    LiftRules.setSiteMap(entries)
  }
}

