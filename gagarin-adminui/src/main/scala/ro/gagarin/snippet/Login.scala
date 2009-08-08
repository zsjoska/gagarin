package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.ro.gagarin.wsclient.WSClient
import _root_.ro.gagarin.model.WebServiceClient._
import _root_.ro.gagarin.model.{wsSessionId, SessionInfo}

class Login {
    def userinfo(in: NodeSeq): NodeSeq  = {
      if(wsSessionId.is != null){
        <span>
        Welcome { wsSessionId.user.getName() }
        {
    	link("login", () => {
    	  getAuthentication.logout(wsSessionId.session)
    	  wsSessionId.set(null)
    	}, Text("Logout"))
    	}
        </span>
      } else NodeSeq.Empty
    }

  def login (in: NodeSeq): NodeSeq  = {
    var u:String = null
    var p: String = null
    val session = getAuthentication.createSession(null, null)
    bind("login", in, 
         "username" -> text("", (x)=> (u=x)),
         "password" -> password("", (x) =>(p=x)),
         "submit" -> submit("Login", () => {
           try{
	           val user = getAuthentication.login(session, u, p, null)
	           wsSessionId.set(SessionInfo(session,user))
               notice("Logged in " + u + "("+p+")" + " session=" + session)
           } catch {
             case e: ItemNotFoundException_Exception => {
	           notice("Login failed")
	           redirectTo("/login")
             }
             case e: Exception => {
               // TODO: This is ugly: Server error:ro.gagarin.exceptions.SessionNotFoundException
	           notice("Server error:" + e.getMessage())
	           redirectTo("/login")
             }
           }})
    )
  } 
}

