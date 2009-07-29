package ro.gagarin.snippet

import _root_.scala.xml.{NodeSeq, Text, Group, Node}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._

class Login {
  def login (in: NodeSeq): NodeSeq  = {
    var u:String = null
    var p: String = null
    bind("login", in, 
         "username" -> text("", (x)=> (u=x)),
         "password" -> password("", (x) =>(p=x)),
         "submit" -> submit("Login", () => {
           notice("Logged in " + u + "("+p+")")
           redirectTo("/") })
    )
  } 
}

