package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._

/**
  * A class that's instantiated early and run.  It allows the application
  * to modify lift's environment
  */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("ro.gagarin")

    // Build SiteMap
    val entries = SiteMap( Menu(Loc("Home", List("index"), "Home")),
    					   Menu(Loc("login", List("login"), "Login")),
    					   Menu(Loc("users", List("users"), "Users"),
	    					   Menu(Loc("listUsers", List("users"), "List Users")),
	    					   Menu(Loc("newUser", List("newUser"), "New User")),
	    					   Menu(Loc("editUser", List("editUser"), "Edit User"))
    					   )
    					)
    LiftRules.setSiteMap(entries)
    
    LiftRules.dispatch.prepend(NamedPF("Login Validation") {
        case Req("*" :: page , "", _)
          if page.head != "login"  => 
          () => {
            println(">>>>>>>>>>>>>>>>>>"+page.head)
            Full(RedirectResponse("/login"))
          }
      })
                                        
//    LiftRules.dispatch.prepend(NamedPF("Login Validation") {
//        case Req("login" :: page , "", _)
//          if !LoginStuff.is && page.head != "validate" =>
//          () => Full(RedirectResponse("/login/validate"))
//      })
  }
}

