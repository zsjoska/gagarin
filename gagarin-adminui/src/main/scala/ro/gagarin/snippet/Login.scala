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

class Login {
  def login (in: NodeSeq): NodeSeq  = {
    var u:String = null
    var p: String = null
    bind("login", in, 
         "username" -> text("", (x)=> (u=x)),
         "password" -> password("", (x) =>(p=x)),
         "submit" -> submit("Login", () => {
           val session = WSClient.getWSClient("http://localhost:8080/ws/").getAuthentication().createSession(null, null)
           notice("Logged in " + u + "("+p+")" + " session=" + session)
           redirectTo("/") })
    )
  } 
}

