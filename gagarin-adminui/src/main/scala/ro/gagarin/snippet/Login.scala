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

object SessionId extends SessionVar("")

class Login {
  def login (in: NodeSeq): NodeSeq  = {
    var u:String = null
    var p: String = null
    bind("login", in, 
         "username" -> text("", (x)=> (u=x)),
         "password" -> password("", (x) =>(p=x)),
         "submit" -> submit("Login", () => {
           try{
	           val session = getAuthentication.createSession(null, null)
	           getAuthentication.login(session, u, p, null);
	           SessionId.set(session)
               notice("Logged in " + u + "("+p+")" + " session=" + session)
               redirectTo("/") 
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

