package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.ro.gagarin.model.{wsSessionId, SessionInfo}

/**
  * A class that's instantiated early and run.  It allows the application
  * to modify lift's environment
  */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("ro.gagarin")

    // check for login on the pages
  	def requiresLogin = If(()=>wsSessionId.is!=null, () => RedirectResponse("/login"))
    def loggedIn = If(()=>wsSessionId.is==null, () => RedirectResponse("/"))
    
    // Build SiteMap
    val entries = SiteMap( Menu(Loc("Home", List("index"), "Home", requiresLogin)),
    					   Menu(Loc("login", List("login"), "Login", loggedIn)),
    					   Menu(Loc("users", List("users"), "Users", requiresLogin),
	    					   Menu(Loc("listUsers", List("users"), "List Users", requiresLogin)),
	    					   Menu(Loc("newUser", List("newUser"), "New User", requiresLogin)),
	    					   Menu(Loc("editUser", List("editUser"), "Edit User", requiresLogin))
    					   )
    					)
    LiftRules.setSiteMap(entries)
  }
}

