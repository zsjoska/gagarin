package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.ro.gagarin.model.{wsSession, SessionInfo}
import _root_.ro.gagarin.PermissionEnum
import _root_.ro.gagarin.PermissionEnum._
import _root_.net.liftweb.http.S._


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
    def rqPerm(p:PermissionEnum) = If(()=>{
    	
    	if(wsSession.permMap.get(1) == None){
    	    wsSession.set(null)
    	    error("Could not get the user permissions");
    	    redirectTo("/login")
    	}
    	wsSession.permMap.get(1).get.contains(p)
     }, () => RedirectResponse("/"))
    
    // Build SiteMap
    val entries = SiteMap( 
      Menu(Loc("Home", List("index"), "Home", requiresLogin)),
      Menu(Loc("login", List("login"), "Login", loggedIn)),
      Menu(Loc("users", List("users"), "Users", requiresLogin, rqPerm(LIST)),
	      Menu(Loc("newUser", List("newUser"), "New User", requiresLogin, rqPerm(CREATE))),
	      Menu(Loc("editUser", List("editUser"), "Edit User", Hidden, requiresLogin, rqPerm(UPDATE)))
      ),
      Menu(Loc("roles", List("roles"), "Roles", requiresLogin, rqPerm(LIST)),
	      Menu(Loc("newRole", List("newRole"), "New Role", requiresLogin, rqPerm(CREATE))),
	      Menu(Loc("editRole", List("editRole"), "Edit Role", Hidden, requiresLogin, rqPerm(UPDATE)))
      ),
      Menu(Loc("groups", List("groups"), "Groups", requiresLogin),
	      Menu(Loc("newGroup", List("newGroup"), "New Group", requiresLogin)),
	      Menu(Loc("editGroup", List("editGroup"), "Edit Group", Hidden, requiresLogin, rqPerm(UPDATE)))
      ),
      Menu(Loc("monitor", List("monitor"), "Monitor", requiresLogin, rqPerm(ADMIN)),
	      Menu(Loc("sessions", List("sessions"), "Sessions", requiresLogin, rqPerm(ADMIN))),
	      Menu(Loc("statistics", List("statistics"), "Statistics", requiresLogin, rqPerm(ADMIN))),
	      Menu(Loc("logs", List("logs"), "Logs", requiresLogin, rqPerm(ADMIN)))
      ),
      Menu(Loc("config", List("config"), "Config", requiresLogin, rqPerm(ADMIN)))
    )
    
    LiftRules.setSiteMap(entries)
  }
}

