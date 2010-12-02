package ro.gagarin.snippet

import scala.xml.{NodeSeq, Text, Group, Node}
import net.liftweb.http._
import net.liftweb.http.S
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.util.Helpers._
import net.liftweb.util._
import ro.gagarin.model.authService
import ro.gagarin.model.{wsSession, SessionInfo}

class Login {
    def userinfo(in: NodeSeq): NodeSeq  = {
      if(wsSession.is != null){
        <span>
        Welcome { wsSession.user.getName() }
        {
    	link("login", () => {
    	  authService.logout
    	}, Text("Logout"))
    	}
        </span>
      } else NodeSeq.Empty
    }

  def login (in: NodeSeq): NodeSeq  = {
    var u:String = null
    var p: String = null
    val session = authService.createSession(null)
    bind("login", in, 
         "username" -> text("", (x)=> (u=x)),
         "password" -> password("", (x) =>(p=x)),
         "submit" -> submit("Login", () => {
	           val user = authService.login(session, u, p)
               notice("Logged in " + u + "("+p+")" + " session=" + session)
          })
    )
  } 
}

