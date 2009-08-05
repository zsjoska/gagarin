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
    def loginRequires(l: Boolean) = If(()=>(wsSessionId==null)==l, () => RedirectResponse("/login"))
    
    // Build SiteMap
    val entries = SiteMap( Menu(Loc("Home", List("index"), "Home", loginRequires(true))),
    					   Menu(Loc("login", List("login"), "Login", loginRequires(false))),
    					   Menu(Loc("users", List("users"), "Users", loginRequires(true)),
	    					   Menu(Loc("listUsers", List("users"), "List Users", loginRequires(true))),
	    					   Menu(Loc("newUser", List("newUser"), "New User", loginRequires(true))),
	    					   Menu(Loc("editUser", List("editUser"), "Edit User", loginRequires(true)))
    					   )
    					)
    LiftRules.setSiteMap(entries)
  }
}

